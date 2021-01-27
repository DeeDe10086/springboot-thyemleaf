package com.lagou.homework.thymeleaf.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.homework.thymeleaf.entity.TArticle;
import com.lagou.homework.thymeleaf.service.TArticleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlogController {

    private Integer page = 1;
    private Integer pageSize = 2;

    private final TArticleService tArticleService;

    public BlogController(@Qualifier("tArticleService") TArticleService tArticleService) {
        this.tArticleService = tArticleService;
    }

    @RequestMapping("blog")
    public ModelAndView toSuccess(Integer page, Integer pageSize){
        if(page==null){
            page = this.page;
        }
        if(pageSize==null){
            pageSize = this.pageSize;
        }
        ModelAndView modelAndView = new ModelAndView();

        /**
         * 获取页面数据
         */
        Page<TArticle> pages= new Page<>(page, pageSize);
        tArticleService.getArticles(pages);
        /**
         * 数据包装到ModelAndView
         */
        modelAndView.addObject("result",pages);

        modelAndView.setViewName("client/index");
        return modelAndView;
    }
}
