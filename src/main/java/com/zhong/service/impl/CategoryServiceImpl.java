package com.zhong.service.impl;

import com.zhong.mapper.ArticleMapper;
import com.zhong.mapper.CategoryMapper;
import com.zhong.pojo.Category;
import com.zhong.service.CategoryService;
import com.zhong.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void addCategory(Category category) {
        // 从ThreadLocal中获取用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        
        // 设置创建用户ID
        category.setCreateUser(userId);
        
        // 调用Mapper插入数据
        categoryMapper.insert(category);
    }

    @Override
    public List<Category> getCategoryList() {
        // 从ThreadLocal中获取用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        // 根据用户ID查询分类列表
        return categoryMapper.findByUserId(userId);
    }

    @Override
    public Category getCategoryDetail(Integer id) {
        // 从ThreadLocal中获取用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        // 根据ID查询分类详情
        Category category = categoryMapper.findById(id);

        // 验证分类是否存在且属于当前用户
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        if (!category.getCreateUser().equals(userId)) {
            throw new RuntimeException("无权访问该分类");
        }

        return category;
    }

    @Override
    public void updateCategory(Category category) {
        // 从ThreadLocal中获取用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        // 根据ID查询分类详情，验证权限,查看当前的id是否有对应的分类
        Category existingCategory = categoryMapper.findById(category.getId());

        // 验证分类是否存在且属于当前用户
        if (existingCategory == null) {
            throw new RuntimeException("分类不存在");
        }

        // 不能更新其他的用户的分类
        if (!existingCategory.getCreateUser().equals(userId)) {
            throw new RuntimeException("无权更新该分类");
        }

        // 调用Mapper更新数据
        categoryMapper.update(category);
    }

    @Override
    public void deleteCategoryByName(String categoryName) {
        // 从ThreadLocal中获取用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        // 验证分类是否存在且属于当前用户
        Category category = categoryMapper.findByNameAndUserId(categoryName, userId);
        if (category == null) {
            throw new RuntimeException("分类不存在或无权删除");
        }

        // 检查该分类下是否有文章
        Integer articleCount = articleMapper.countByCategoryId(category.getId(), userId);
        if (articleCount != null && articleCount > 0) {
            throw new RuntimeException("该分类下有 " + articleCount + " 篇文章，无法删除。请先删除或转移文章。");
        }

        // 调用Mapper删除数据
        categoryMapper.deleteByNameAndUserId(categoryName, userId);
    }
}
