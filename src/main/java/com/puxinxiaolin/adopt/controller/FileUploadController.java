package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController {
    
    private final FileUploadService fileUploadService;
    
    /**
     * 上传图片
     */
    @PostMapping("/upload/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.uploadFile(file, "images");
        return Result.success("上传成功", fileUrl);
    }
}
