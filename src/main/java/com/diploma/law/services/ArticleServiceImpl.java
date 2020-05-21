package com.diploma.law.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diploma.law.DAO.ArticleDAO;
import com.diploma.law.models.ArticlesEntity;

@Service("ArticleService")
@Transactional
public class ArticleServiceImpl implements ArticleService
{

    @Autowired
    ArticleDAO article;

    @Override
    public ArticlesEntity findById(int id)
    {
        return article.findById(id);
    }

    @Override
    public ArticlesEntity FindByTitle(String title)
    {
        return article.FindByTitle(title);
    }

    @Override
    public List<ArticlesEntity> findAllArticles()
    {
        return article.findAllArticles();
    }

    @Override
    public void deleteTask(int id)
    {
        article.deleteTask(id);
    }

    @Override
    public void saveArticle(ArticlesEntity articlesEntity)
    {
        article.saveArticle(articlesEntity);
    }
}
