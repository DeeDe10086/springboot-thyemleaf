package com.lagou.homework.thymeleaf.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.homework.thymeleaf.entity.TArticle;
import com.lagou.homework.thymeleaf.mapper.TArticleMapper;
import com.lagou.homework.thymeleaf.service.TArticleService;
import org.springframework.stereotype.Service;

/**
 * (TArticle)表服务实现类
 *
 * @author makejava
 * @since 2021-01-27 15:24:02
 */
@Service("tArticleService")
public class TArticleServiceImpl extends ServiceImpl<TArticleMapper, TArticle> implements TArticleService {

    @Override
    public Page<TArticle> getArticles(Page<TArticle> page){
        /**
         * 直接引用mybatis-plus的分页方法
         */
        super.page(page);
        return page;
    }
}