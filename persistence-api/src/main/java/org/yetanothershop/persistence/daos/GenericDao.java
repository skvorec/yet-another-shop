package org.yetanothershop.persistence.daos;

import org.yetanothershop.persistence.entities.Identifiable;

/**
 *
 */
public interface GenericDao<T extends Identifiable>
{
    T createOrUpdate(T entity);


    T findById(Long id);
}