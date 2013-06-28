package org.yetanothershop.persistence.daos;

import java.util.List;
import org.yetanothershop.persistence.entities.SAttribute;

/**
 *
 */
public interface SAttributeDao extends GenericDao<SAttribute>
{
    List<SAttribute> findByObjectType(Long objTypeId);
}