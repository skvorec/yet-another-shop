package org.yetanothershop.persistence.daos;

import org.yetanothershop.persistence.entities.SAttribute;

/**
 *
 */
public class SAttributeDaoImpl extends GenericDaoImpl<SAttribute> implements SAttributeDao
{
    public SAttributeDaoImpl(Class<SAttribute> type)
    {
        super(type);
    }
}