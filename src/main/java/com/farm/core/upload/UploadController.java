package com.farm.core.upload;

import com.farm.base.common.JsonResult;
import com.farm.base.exception.UploadException;
import com.farm.core.upload.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 ** @Version 1.0.0
 */
@RestController
@RequestMapping("api")
public class UploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    @PostMapping("upload/single")
    public JsonResult importImage(@RequestParam("file") MultipartFile file) throws IOException, UploadException {
        LOGGER.info("请求开始报告 : 上传单个文件接口");
        String fileName = this.uploadService.uploadFile(file);
        Map<String, Object> map = new HashMap<>();
        map.put("url", fileName);
        return JsonResult.ok(map);
    }


}
