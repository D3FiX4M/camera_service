package ru.d3fix4m.camera_service.util;

import lombok.experimental.UtilityClass;
import ru.d3fix4m.camera_service.ImageDataResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@UtilityClass
public class CompressingImageUtil {


    public byte[] compress(ImageDataResponse imageData) throws IOException {
        byte[] uncompressedImage = imageData.getImage().toByteArray();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        deflaterOutputStream.write(uncompressedImage);
        deflaterOutputStream.close();
        byte[] compressedImage = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return compressedImage;
    }

    public byte[] decompress(byte[] compressedImage) throws IOException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedImage);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;
        while ((read = inflaterInputStream.read()) != -1) {
            byteArrayOutputStream.write(read);
        }
        inflaterInputStream.close();
        byteArrayInputStream.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
