package org.yetanothershop.persistence.daos;

import java.util.List;
import org.yetanothershop.persistence.entities.BaseEntity;

/**
 *
 */
public interface GenericDao<T extends BaseEntity>
{
    T createOrUpdate(T entity);


    T merge(T entity);


    void delete(T entity);


    T findById(Long id);


    T findByName(String name);


    List<T> findByPartOfName(String namePart);
}