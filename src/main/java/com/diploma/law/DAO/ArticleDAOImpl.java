package com.diploma.law.DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.diploma.law.models.ArticlesEntity;

@Repository("ArticleDAO")
public class ArticleDAOImpl extends AbstractDAO<Integer, ArticlesEntity> implements ArticleDAO
{
    @Override
    public ArticlesEntity findById(int id)
    {
        Criteria criteria = getSession().createCriteria(ArticlesEntity.class);
        criteria.add(Restrictions.eq("idArticle", id));
        return (ArticlesEntity)criteria.uniqueResult();
    }

    @Override
    public ArticlesEntity FindByTitle(String title)
    {
        Criteria criteria = getSession().createCriteria(ArticlesEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (ArticlesEntity)criteria.uniqueResult();
    }

    @Override
    public List<ArticlesEntity> findAllArticles()
    {
        Criteria criteria = getSession().createCriteria(ArticlesEntity.class);
        return (List<ArticlesEntity>)criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public void deleteTask(int id)
    {
        Query query = getSession().createSQLQuery("DELETE from Articles where idArticle=:id");
        query.setInteger("id", id);
        query.executeUpdate();
    }

    @Override
    public void saveArticle(ArticlesEntity articlesEntity)
    {

        getSession().saveOrUpdate(articlesEntity);
    }
}
