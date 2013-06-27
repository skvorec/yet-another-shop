package org.yetanothershop.persistence.daos;

/**
 *
 */
public interface GenericDao<T>
{
    T create(T entity);
}