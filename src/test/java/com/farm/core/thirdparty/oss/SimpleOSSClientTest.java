package com.farm.core.thirdparty.oss;

import com.aliyun.oss.OSSClient;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.OutputStream;

import static com.farm.core.TestUtils.loadImage;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SimpleOSSClientTest {

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    @Autowired
    private OSSClientFactory ossClientFactory;

    private static byte[] idCardImage;

    private OSSPath formalOssPath = OSSPathBuilder.create().withDirectory(OSSClientFactory.Directory.FILES).withDataType(OSSClientFactory.DataType.IMAGE, "1").withFilename("id_card.jpg").build();
    private OSSPath renamedFormalOssPath = OSSPathBuilder.create().withDirectory(OSSClientFactory.Directory.FILES)
                                                         .withDataType(OSSClientFactory.DataType.IMAGE, "2")
                                                         .withFilename("id_card_new.jpg").build();
    private OSSPath temporaryOssPath = OSSPathBuilder.create().withDirectory(OSSClientFactory.Directory.FILES).withDataType(OSSClientFactory.DataType.TEMP, "1").withFilename("id_card.jpg").build();


    @BeforeClass
    public static void init() throws IOException {
        idCardImage = loadImage("/images/image_id_card_front.jpg");
    }

    @Test
    public void testSave() throws Exception {
        SimpleOSSClient ossClient = ossClientFactory.createOSSClient();
        OSSPath ossPath = new OSSPath();
        ossPath.setDirectory(OSSClientFactory.Directory.FILES);
        ossPath.setDataType(OSSClientFactory.DataType.IMAGE);
        ossPath.setDataId("500");
        ossPath.setFilename("test.jpg");

        boolean fileExist = ossClient.isFileExist(ossPath);
        System.out.println(fileExist);


        System.out.println(ossPath.toString());
        ossClient.save(ossPath,idCardImage);

        boolean isfile = ossClient.isFileExist(ossPath);
        System.out.println(isfile);

    }

    @Test
    public void testMove() {

        OSSPath ossPath = new OSSPath();
        ossPath.setDirectory(OSSClientFactory.Directory.FILES);
        ossPath.setDataType(OSSClientFactory.DataType.IMAGE);
        ossPath.setDataId("400");
        ossPath.setFilename("test.jpg");

        SimpleOSSClient ossClient = ossClientFactory.createOSSClient();
//        ossClient.save(ossPath, idCardImage);

        ossClient.delete(ossPath);
    }

    @Test
    public void testRename() {
        SimpleOSSClient ossClient = ossClientFactory.createOSSClient();


        OSSPath ossPath = new OSSPath();
        ossPath.setDirectory(OSSClientFactory.Directory.FILES);
        ossPath.setDataType(OSSClientFactory.DataType.IMAGE);
        ossPath.setDataId("400");
        ossPath.setFilename("test.jpg");

        ossClient.save(ossPath, idCardImage);

        ossClient.rename(OSSClientFactory.Directory.FILES, OSSClientFactory.Directory.FILES,
                         OSSClientFactory.DataType.IMAGE, "400", "test.jpg", "test1.jpg");
    }


    @Test
    public void testDownload() throws Exception{
        SimpleOSSClient ossClient = ossClientFactory.createOSSClient();

        OSSPath ossPath = new OSSPath();
        ossPath.setDirectory(OSSClientFactory.Directory.FILES);
        ossPath.setDataType(OSSClientFactory.DataType.IMAGE);
        ossPath.setDataId("400");
        ossPath.setFilename("test.jpg");
        ossClient.save(ossPath, idCardImage);

        OutputStream outputStream = ossClient.download(ossPath);
        assertNotNull(outputStream);
    }

    @After
    public void clear() {
        OSSClient realOssClient = ossClientFactory.getRealOssClient();
        realOssClient.deleteObject(bucketName, temporaryOssPath.toString());
        realOssClient.deleteObject(bucketName, formalOssPath.toString());
        realOssClient.deleteObject(bucketName, renamedFormalOssPath.toString());
    }

}