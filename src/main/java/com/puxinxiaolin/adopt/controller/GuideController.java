package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.constants.MessageConstant;
import com.puxinxiaolin.adopt.entity.vo.GuideVO;
import com.puxinxiaolin.adopt.service.GuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 指南控制器
 */
@Slf4j
@RestController
@RequestMapping("/guide")
@CrossOrigin
@RequiredArgsConstructor
public class GuideController {
    
    private final GuideService guideService;
    
    /**
     * 获取所有指南分类
     */
    @GetMapping("/categories/list")
    public Result<List<String>> getCategories() {
        List<String> categories = guideService.getAllCategories();
        return Result.success(categories);
    }
}
