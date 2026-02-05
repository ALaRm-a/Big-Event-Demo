package com.zhong.mapper;

import com.zhong.pojo.Article;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ArticleMapper {
    
    /**
     * 插入文章
     * @param article 文章对象
     */
    @Insert("INSERT INTO article (title, content, cover_img, state, category_id, create_user, create_time, update_time) " +
            "VALUES (#{title}, #{content}, #{coverImg}, #{state}, #{categoryId}, #{createUser}, now(), now())")
    void insert(Article article);

    /**
     * 查询文章列表
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param state 文章状态
     * @return 文章列表
     */
    List<Article> list(Integer userId, Integer categoryId, String state);

    /**
     * 根据分类ID统计文章数量
     * @param categoryId 分类ID
     * @param userId 用户ID
     * @return 文章数量
     */
    @Select("SELECT COUNT(*) FROM article WHERE category_id = #{categoryId} AND create_user = #{userId}")
    Integer countByCategoryId(@Param("categoryId") Integer categoryId, @Param("userId") Integer userId);

    /**
     * 根据文章ID查询文章详情
     * @param id 文章ID
     * @return 文章对象
     */
    @Select("SELECT * FROM article WHERE id = #{id}")
    Article findById(Integer id);

    /**
     * 根据文章ID删除文章
     * @param id 文章ID
     * @param userId 用户ID
     */
    @Delete("DELETE FROM article WHERE id = #{id} AND create_user = #{userId}")
    void deleteByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);
}