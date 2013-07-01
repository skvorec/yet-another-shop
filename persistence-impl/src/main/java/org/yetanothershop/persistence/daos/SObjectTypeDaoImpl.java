package org.yetanothershop.persistence.daos;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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


    @Override
    public List<SObjectType> findByName(String name)
    {
        Criteria criteria = createCachedCriteria();
        criteria.add(Restrictions.ilike("name", "%" + name + "%"));
        return criteria.list();
    }
}