package org.yetanothershop.persistence.daos;

import java.util.List;
import org.hibernate.Query;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public class SAttributeDaoImpl extends GenericDaoImpl<SAttribute> implements SAttributeDao
{
    public SAttributeDaoImpl(Class<SAttribute> type)
    {
        super(type);
    }


    @Override
    public List<SAttribute> findByObjectType(Long objTypeId)
    {
        Query query = getSession().createQuery("from SObjectTypeImpl objType where objType.id = :objTypeId");
        query.setParameter("objTypeId", objTypeId);
        return ((SObjectType) query.uniqueResult()).getAssociatedAttrs();
    }
}