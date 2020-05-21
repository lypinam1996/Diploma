package com.diploma.law.DAO;

import java.util.List;

import com.diploma.law.models.ArticlesEntity;

public interface ArticleDAO
{
    ArticlesEntity findById(int id);

    ArticlesEntity FindByTitle(String title);

    List<ArticlesEntity> findAllArticles();

    void deleteTask(int id);

    void saveArticle(ArticlesEntity articlesEntity);
}
