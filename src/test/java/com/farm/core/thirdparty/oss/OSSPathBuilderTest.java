package com.farm.core.thirdparty.oss;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class OSSPathBuilderTest {

    @Test
    public void testBuildOssPath() {
        OSSPath ossPath = OSSPathBuilder.create()
                .withDirectory(OSSClientFactory.Directory.FILES)
                .withDataType(OSSClientFactory.DataType.IMAGE, "1234567890")
                .withFilename("abc.txt").build();

        assertEquals("files/image/1234567890/abc.txt", ossPath.toString());
    }

}