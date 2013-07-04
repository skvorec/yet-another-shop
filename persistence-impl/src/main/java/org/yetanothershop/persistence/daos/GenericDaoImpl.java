package org.yetanothershop.persistence.daos;

import java.util.List;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yetanothershop.persistence.entities.BaseEntity;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
@Transactional(propagation = Propagation.MANDATORY)
public class GenericDaoImpl<T extends BaseEntity> extends HibernateDaoSupport implements GenericDao<T>
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
        Criteria criteria = createCachedCriteria();
        criteria.add(Restrictions.eq("id", id));
        return (T) criteria.uniqueResult();
    }


    @Override
    public T findByName(String name)
    {
        Criteria criteria = createCachedCriteria();
        criteria.add(Restrictions.eq("name", name));
        return (T) criteria.uniqueResult();
    }


    @Override
    public List<T> findByPartOfName(String name)
    {
        Criteria criteria = createCachedCriteria();
        criteria.add(Restrictions.ilike("name", "%" + name + "%"));
        return criteria.list();
    }


    protected Criteria createCachedCriteria()
    {
        final Criteria criteria = getSession().createCriteria(type);
        criteria.setCacheable(true);
        criteria.setCacheMode(CacheMode.NORMAL);
        return criteria;
    }
}