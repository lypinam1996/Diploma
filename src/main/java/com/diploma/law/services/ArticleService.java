package com.diploma.law.services;
import com.diploma.law.models.ArticlesEntity;
import java.util.List;

public interface ArticleService {
    ArticlesEntity findById(int id);
    ArticlesEntity FindByTitle(String title);
    List<ArticlesEntity> findAllArticles();
    void deleteTask(int id);
    void saveArticle(ArticlesEntity articlesEntity);
}
