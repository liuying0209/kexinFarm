/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.farm.core.thirdparty.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 */
public class SimpleOSSClient {

    private static final Logger LOGGER = LoggerFactory.getLogger("aliyun-oss-logger");

    private String bucketName;
    private OSSClient ossClient;

    SimpleOSSClient(String bucketName, OSSClient ossClient) {
        this.bucketName = bucketName;
        this.ossClient = ossClient;
    }

    public String save(OSSPath ossPath, byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        return this.save(ossPath,is);
    }

    public String save(OSSPath ossPath, InputStream is) {
        PutObjectResult result = ossClient.putObject(bucketName, ossPath.toString(), is);
        LOGGER.info("OSS|PUT_OBJECT|{}|{}", result.getRequestId(), ossPath.toString());
        return ossPath.toString();
    }
    public String save(String ossPath, byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        PutObjectResult result = ossClient.putObject(bucketName, ossPath, is);
        return ossPath;
    }

    public void move(OSSClientFactory.Directory fromDir, OSSClientFactory.Directory toDir, OSSClientFactory.DataType dataType, String dataId, String... filenames) {
        for (String filename : filenames) {
            OSSPath fromPath = OSSPathBuilder.create().withDirectory(fromDir).withDataType(dataType, dataId).withFilename(filename).build();
            OSSPath toPath = OSSPathBuilder.create().withDirectory(toDir).withDataType(dataType, dataId).withFilename(filename).build();
            CopyObjectResult copyObjectResult = ossClient.copyObject(bucketName, fromPath.toString(), bucketName, toPath.toString());
            LOGGER.info("OSS|COPY_OBJECT|{}|{}|{}", copyObjectResult.getRequestId(), fromPath.toString(), toPath.toString());
            ossClient.deleteObject(bucketName, fromPath.toString());
            LOGGER.info("OSS|DELETE_OBJECT|{}", fromPath.toString());
        }
    }

    public void delete(OSSPath path) {
        if (ossClient.doesObjectExist(bucketName, path.toString())) {
            ossClient.deleteObject(bucketName, path.toString());
            LOGGER.info("OSS|DELETE OBJECT|{}", path.toString());
            return;
        }
        LOGGER.info("OSS|DELETE OBJECT|ERROR|OBJECT IS NOT EXISTS|{}", path.toString());
    }

    public void rename(OSSClientFactory.Directory fromDir, OSSClientFactory.Directory toDir,
                       OSSClientFactory.DataType dataType, String dataId, String oldFilename, String newFilename) {
        OSSPath fromPath =
                OSSPathBuilder.create().withDirectory(fromDir).withDataType(dataType, dataId).withFilename(oldFilename)
                              .build();
        if (isFileExist(fromPath)) {
            OSSPath toPath =
                    OSSPathBuilder.create().withDirectory(toDir).withDataType(dataType, dataId).withFilename(newFilename)
                            .build();
            CopyObjectResult copyObjectResult =
                    ossClient.copyObject(bucketName, fromPath.toString(), bucketName, toPath.toString());
            LOGGER.info("OSS|COPY_OBJECT|{}|{}|{}", copyObjectResult.getRequestId(), fromPath.toString(),
                    toPath.toString());
        }
    }


    public ByteArrayOutputStream download(OSSPath path) throws IOException {
        return download(path.toString());
    }

    public ByteArrayOutputStream download(String ossPath) throws IOException {
        OSSObject ossObject = ossClient.getObject(bucketName, ossPath);
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n;
        try {
            inputStream = ossObject.getObjectContent();
            while((n = inputStream.read(buf, 0, buf.length)) != -1) {
                outputStream.write(buf, 0, n);
            }
            LOGGER.info("OSS|DOWNLOAD_OBJECT|{}", ossPath);
        } catch (Exception e) {
            LOGGER.error("OSS|DOWNLOAD_OBJECT|ERROR" + ossPath, e);
        } finally {
            inputStream.close();
            ossObject.getObjectContent().close();
        }
        return outputStream;
    }

    public boolean isFileExist(OSSPath ossPath) {
        return ossClient.doesObjectExist(bucketName, ossPath.toString());
    }

    public boolean isFileExist(String key) {
        return ossClient.doesObjectExist(bucketName, key);
    }

    public long fileSize(OSSPath ossPath) {
        if (!isFileExist(ossPath)) {
            return 0;
        }
        ObjectMetadata objectMetadata = ossClient.getObjectMetadata(bucketName, ossPath.toString());
        if (objectMetadata == null) {
            return 0;
        }
        return objectMetadata.getContentLength();
    }

    public String getMd5(OSSPath ossPath) {
        if (!isFileExist(ossPath)) {
            return "";
        }
        ObjectMetadata objectMetadata = ossClient.getObjectMetadata(bucketName, ossPath.toString());
        if (objectMetadata == null) {
            return "";
        }
        return objectMetadata.getContentMD5();
    }

    public String getUrl(OSSPath ossPath) {
        return getUrl(ossPath.toString());
    }

    public String getUrl(String key) {
        // 设置URL过期时间为1小时  3600l* 1000
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        LOGGER.info("OSS|GETURL|{}|{}", key, url.toString());

        if (url != null) {
            //如果是阿里云内网访问地址，替换 -internal
            String result = url.toString();
            result = result.replace("-internal", "");
            return result;
        }
        return null;
    }

    public void copy(OSSClientFactory.Directory fromDir, OSSClientFactory.Directory toDir, OSSClientFactory.DataType fromDataType, OSSClientFactory.DataType toDataType, String fromDataId, String toDataId, String... filenames) {
        for (String filename : filenames) {
            OSSPath fromPath = OSSPathBuilder.create().withDirectory(fromDir).withDataType(fromDataType, fromDataId).withFilename(filename).build();
            OSSPath toPath = OSSPathBuilder.create().withDirectory(toDir).withDataType(toDataType, toDataId).withFilename(filename).build();
            CopyObjectResult copyObjectResult = ossClient.copyObject(bucketName, fromPath.toString(), bucketName, toPath.toString());
            LOGGER.info("OSS|COPY_OBJECT|{}|{}|{}", copyObjectResult.getRequestId(), fromPath.toString(), toPath.toString());
        }
    }
}
