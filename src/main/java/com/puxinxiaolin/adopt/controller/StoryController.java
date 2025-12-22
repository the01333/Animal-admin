package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.constants.MessageConstant;
import com.puxinxiaolin.adopt.entity.vo.StoryVO;
import com.puxinxiaolin.adopt.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 故事控制器
 */
@Slf4j
@RestController
@RequestMapping("/story")
@CrossOrigin
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    /**
     * 获取所有故事分类
     */
    @GetMapping("/categories/list")
    public Result<List<String>> getCategories() {
        List<String> categories = storyService.getAllCategories();
        return Result.success(categories);
    }
}
