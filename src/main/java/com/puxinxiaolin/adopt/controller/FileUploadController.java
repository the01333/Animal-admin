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
    
    /**
     * 上传头像
     */
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.uploadFile(file, "avatars");
        return Result.success("上传成功", fileUrl);
    }
    
    /**
     * 上传文档
     */
    @PostMapping("/upload/document")
    public Result<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.uploadFile(file, "documents");
        return Result.success("上传成功", fileUrl);
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public Result<String> deleteFile(@RequestParam String fileUrl) {
        boolean success = fileUploadService.deleteFile(fileUrl);
        return success ? Result.success("删除成功", null) : Result.error("删除失败");
    }
}
