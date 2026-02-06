package com.zhong.service;

import com.zhong.pojo.Article;
import com.zhong.pojo.PageBean;

public interface ArticleService {
    
    /**
     * 添加文章
     * @param article 文章对象
     */
    void addArticle(Article article);

    /**
     * 获取文章列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param categoryId 分类ID
     * @param state 文章状态
     * @return 分页结果
     */
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    /**
     * 根据文章ID删除文章
     * @param id 文章ID
     */
    void deleteArticle(Integer id);

    /**
     * 更新文章
     * @param article 文章对象
     */
    void updateArticle(Article article);
}