package com.diploma.law.services;

import java.util.List;

import com.diploma.law.models.ArticlesEntity;

public interface ArticleService
{
    ArticlesEntity findById(int id);

    ArticlesEntity FindByTitle(String title);

    List<ArticlesEntity> findAllArticles();

    void deleteTask(int id);

    void saveArticle(ArticlesEntity articlesEntity);
}
