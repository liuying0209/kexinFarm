package com.farm.core.thirdparty.oss;

import com.aliyun.oss.OSSClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class OSSClientFactoryTest {

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    @Autowired
    private OSSClientFactory ossClientFactory;

    @Test
    public void testOSSClientFactoryInit() {
        OSSClient realOssClient = ossClientFactory.getRealOssClient();
        Assert.assertTrue(realOssClient.doesBucketExist(bucketName));
    }

//    @After
//    public void clear() {
//        ossClientFactory.getRealOssClient().deleteBucket(bucketName);
//    }

}