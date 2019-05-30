/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.farm.core;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;


public class TestUtils {

    public static byte[] loadImage(String imagePath) {
        byte[] imageBytes;
        try (InputStream imageInputStream = TestUtils.class.getResourceAsStream(imagePath)) {
            ByteArrayOutputStream imageByteArrayOS = new ByteArrayOutputStream();
            IOUtils.copy(imageInputStream, imageByteArrayOS);
            imageBytes = imageByteArrayOS.toByteArray();
        } catch (IOException e) {
            imageBytes = new byte[0];
        }
        return imageBytes;
    }
}
