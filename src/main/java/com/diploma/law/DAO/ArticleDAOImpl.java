package com.diploma.law.DAO;
import com.diploma.law.models.ArticlesEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ArticleDAO")
public class ArticleDAOImpl extends AbstractDAO<Integer,ArticlesEntity> implements ArticleDAO{
    @Override
    public ArticlesEntity findById(int id) {
        Criteria criteria = getSession().createCriteria(ArticlesEntity.class);
        criteria.add(Restrictions.eq("idArticle", id));
        return (ArticlesEntity) criteria.uniqueResult();
    }

    @Override
    public ArticlesEntity FindByTitle(String title) {
        Criteria criteria = getSession().createCriteria(ArticlesEntity.class);
        criteria.add(Restrictions.eq("title", title));
        return (ArticlesEntity) criteria.uniqueResult();
    }

    @Override
    public List<ArticlesEntity> findAllArticles() {
        Criteria criteria = getSession().createCriteria(ArticlesEntity.class);
        return (List<ArticlesEntity>) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }
}
