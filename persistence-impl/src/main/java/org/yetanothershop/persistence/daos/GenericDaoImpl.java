package org.yetanothershop.persistence.daos;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yetanothershop.persistence.entities.Identifiable;

/**
 *
 */
@Transactional(propagation = Propagation.MANDATORY)
public class GenericDaoImpl<T extends Identifiable> extends HibernateDaoSupport implements GenericDao<T>
{
    private final Class<T> type;


    public GenericDaoImpl(Class<T> type)
    {
        this.type = type;
    }


    @Override
    public T createOrUpdate(T entity)
    {
        getSession().saveOrUpdate(entity);
        return entity;
    }


    @Override
    public T findById(Long id)
    {
        Criteria criteria = createCachedCriteria(type);
        criteria.add(Restrictions.eq("id", id));
        return (T) criteria.uniqueResult();
    }


    protected Criteria createCachedCriteria(Class clazz)
    {
        final Criteria criteria = getSession().createCriteria(clazz);
        criteria.setCacheable(true);
        criteria.setCacheMode(CacheMode.NORMAL);
        return criteria;
    }
}