package org.yetanothershop.persistence.daos;

import java.util.List;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public interface SObjectTypeDao extends GenericDao<SObjectType>
{
    List<SObjectType> findAll();
}