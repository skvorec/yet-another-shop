package org.yetanothershop.persistence.daos;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class GenericDaoImpl<T> extends HibernateDaoSupport implements GenericDao<T>
{
    @Override
    @Transactional
    public T create(T entity)
    {
        getSession().saveOrUpdate(entity);
        return entity;
    }
}