package com.lagou.homework.thymeleaf.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.homework.thymeleaf.entity.TArticle;

/**
 * (TArticle)表服务接口
 *
 * @author makejava
 * @since 2021-01-27 15:23:59
 */
public interface TArticleService extends IService<TArticle> {

    Page<TArticle> getArticles(Page<TArticle> page);

}