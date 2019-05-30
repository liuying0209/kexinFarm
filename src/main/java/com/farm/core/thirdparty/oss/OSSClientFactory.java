/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.farm.core.thirdparty.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.SetBucketLifecycleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Aliyun OSS客户端工厂
 *
 */
@Service
public class OSSClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger("aliyun-oss-logger");

    @Value("${aliyun.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.access-key-secret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    private OSSClient ossClient;

    @PostConstruct
    public void initOSSClient() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 构建Bucket，及相应规则
        boolean isBucketExist = ossClient.doesBucketExist(bucketName);
        if (!isBucketExist) {
            Bucket bucket = ossClient.createBucket(bucketName);
            LOGGER.info("OSS|CREATE_BUCKET|{}", bucket);

            //设置bucketName 权限为公共读 私有写
//            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            // 设置Bucket生命周期规则
            SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);

            LifecycleRule lifecycleRule = new LifecycleRule("temp_1_day_expire_rule", Directory.FILES.prefix, LifecycleRule.RuleStatus.Enabled, 1);
            request.AddLifecycleRule(lifecycleRule);
            ossClient.setBucketLifecycle(request);
        }
    }

    @PreDestroy
    public void destroyOSSClient() {
        ossClient.shutdown();
    }




    public SimpleOSSClient createOSSClient() {
        return new SimpleOSSClient(bucketName, ossClient);
    }




    /**
     * 获取实际的阿里云OSSClient对象
     *
     * @return 返回<code>OSSClient</code>
     */
    public OSSClient getRealOssClient() {
        return ossClient;
    }

    public enum Directory {
        FILES("files");

        private String prefix;

        Directory(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public enum DataType {
        IMAGE("image"),TEMP("temp");

        private final String prefix;

        DataType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
