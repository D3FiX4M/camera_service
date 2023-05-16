package ru.d3fix4m.camera_service.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.d3fix4m.camera_service.CameraServiceGrpc;
import ru.d3fix4m.camera_service.ImageDataResponse;
import ru.d3fix4m.camera_service.TelemetryData;
import ru.d3fix4m.camera_service.util.CompressingImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@Service
@Slf4j
public class CameraGrpc extends CameraServiceGrpc.CameraServiceImplBase {

    @Override
    public void getCameraImage(Empty request, StreamObserver<ImageDataResponse> responseObserver) {

        try {
            for (int i = 0; i < 5; i++) {
                ImageDataResponse randomImage = getRandomImage();
                ImageDataResponse compressedImage = randomImage.toBuilder()
                        .setImage(ByteString.copyFrom(
                                        CompressingImageUtil.compress(randomImage)
                                )
                        )
                        .build();
                responseObserver.onNext(compressedImage);
            }
        } catch (IOException e) {
            log.error(
                    "Ошибка получения данных: " + e.getMessage()
            );
        }
        responseObserver.onCompleted();
    }

    private ImageDataResponse getRandomImage() {
        int width = new Random().nextInt(769) + 256;
        int height = new Random().nextInt(769) + 256;
        int red = new Random().nextInt(256);
        int green = new Random().nextInt(256);
        int blue = new Random().nextInt(256);
        int color = (red << 16) | (green << 8) | blue;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, color);
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] imageBytes = stream.toByteArray();

        TelemetryData telemetryData = TelemetryData.newBuilder()
                .setHeight(image.getHeight())
                .setWidth(image.getWidth())
                .setSize(imageBytes.length)
                .build();

        return ImageDataResponse.newBuilder()
                .setImage(ByteString.copyFrom(imageBytes))
                .setTelemetry(telemetryData)
                .build();
    }

}
