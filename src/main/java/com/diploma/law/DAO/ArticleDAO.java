package com.diploma.law.DAO;
import com.diploma.law.models.ArticlesEntity;

import java.util.List;

public interface ArticleDAO {
    ArticlesEntity findById(int id);
    ArticlesEntity FindByTitle(String title);
    List<ArticlesEntity> findAllArticles();
    void deleteTask(int id);
    void saveArticle(ArticlesEntity articlesEntity);
}
