package org.yetanothershop.persistence.daos;

import java.util.List;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public class SObjectTypeDaoImpl extends GenericDaoImpl<SObjectType> implements SObjectTypeDao
{
    public SObjectTypeDaoImpl(Class<SObjectType> type)
    {
        super(type);
    }


    @Override
    public List<SObjectType> findAll()
    {
        return getSession().createQuery("from SObjectTypeImpl").list();
    }
}