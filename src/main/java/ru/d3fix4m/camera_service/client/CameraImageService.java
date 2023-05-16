package ru.d3fix4m.camera_service.client;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.d3fix4m.camera_service.CameraServiceGrpc;
import ru.d3fix4m.camera_service.ImageDataResponse;
import ru.d3fix4m.camera_service.TelemetryData;
import ru.d3fix4m.camera_service.client.domain.CameraImage;
import ru.d3fix4m.camera_service.client.domain.CameraImageRepository;
import ru.d3fix4m.camera_service.util.CompressingImageUtil;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class CameraImageService {
    private final CameraServiceGrpc.CameraServiceStub stub;
    private final CameraImageRepository repository;

    public CameraImageService(CameraImageRepository repository) {
        this.stub = CameraServiceGrpc.newStub(
                ManagedChannelBuilder.forAddress("localhost", 9090)
                        .usePlaintext()
                        .build());

        this.repository = repository;
    }

    public void getImages() {

        stub.getCameraImage(Empty.newBuilder().build(), new StreamObserver<>() {
            @Override
            public void onNext(ImageDataResponse imageData) {
                try {
                    byte[] decompressImage = CompressingImageUtil.decompress(imageData.getImage().toByteArray());
                    saveCameraImage(decompressImage, imageData.getTelemetry());
                    log.info("Файл успешно сохранен");
                } catch (IOException e) {
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(
                        "Ошибка получения данных: " + throwable.getMessage()
                );
            }

            @Override
            public void onCompleted() {
                log.info("Все данные успешно получены");

            }
        });
    }


    private void saveCameraImage(byte[] image, TelemetryData telemetryData) {
        repository.save(
                new CameraImage(
                        UUID.randomUUID(),
                        image,
                        telemetryData.getHeight(),
                        telemetryData.getWidth(),
                        telemetryData.getSize()
                )
        );
    }
}
