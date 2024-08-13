package com.crististinca.CarRental.Utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtil {

    private final String DEFAULT_LOC;

    /**
     * @param defaultImagePath a String indicating the path to the default image. Usually in "static/img/{@literal <}name{@literal >}"
     * @throws IOException in case the file was not found.
     */
    public ImageUtil(String defaultImagePath) throws IOException {
        Resource resource = new ClassPathResource(defaultImagePath);
        InputStream input = resource.getInputStream();

        DEFAULT_LOC = Base64.getMimeEncoder().encodeToString(input.readAllBytes());
    }

    public String getImgData(byte[] byteData) {
        if (byteData == null)
            return DEFAULT_LOC;

        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}
