package com.farm.core.upload.service.impl;

import com.farm.base.exception.UploadException;
import com.farm.core.constant.Constants;
import com.farm.core.thirdparty.oss.OSSClientFactory;
import com.farm.core.thirdparty.oss.OSSPath;
import com.farm.core.thirdparty.oss.OSSPathBuilder;
import com.farm.core.thirdparty.oss.SimpleOSSClient;
import com.farm.core.upload.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 ** @Version 1.0.0
 */
@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    private static final FastDateFormat OSS_FORMAT = FastDateFormat.getInstance("yyyyMMdd_HHmmss");


    @Autowired
    private OSSClientFactory ossClientFactory;

    @Value("${oss_url}")
    private  String ossUrl;

    @Override
    public String uploadFile(MultipartFile file) throws IOException, UploadException {
        //判断文件是否为空
        if (file == null) {
            throw new UploadException(UploadException.FILE_IS_ERROE);
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (StringUtils.isBlank(name) || size == 0) {
            throw new UploadException(UploadException.FILE_IS_ERROE);
        }

        String postfix = null;
        int indexOf = StringUtils.lastIndexOf(name, ".");
        //获取文件后缀
        postfix = StringUtils.substring(name, indexOf);


        InputStream inputStream = file.getInputStream();

        BufferedInputStream bis = new BufferedInputStream(inputStream);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateTime = format.format(date);

        String title = "farm_";
        Date day = new Date();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(day);

        title += time;
        title += postfix;

        OSSPath formalOssPath = OSSPathBuilder.create().withDirectory(OSSClientFactory.Directory.FILES).
                withDataType(OSSClientFactory.DataType.IMAGE, dateTime).withFilename(title).build();

        LOGGER.info("保存的路径 : {}", formalOssPath.toString());

        SimpleOSSClient ossClient = ossClientFactory.createOSSClient();
        ossClient.save(formalOssPath, bis);
        String url = ossUrl + formalOssPath.toString();
        return url;

    }
}
