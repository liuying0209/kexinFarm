package com.farm.core.upload.service;

import com.farm.base.exception.UploadException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 ** @Version 1.0.0
 */
public interface UploadService {

    /**
     * 单个文件上传
     */
    String uploadFile(MultipartFile file) throws IOException, UploadException;


}
