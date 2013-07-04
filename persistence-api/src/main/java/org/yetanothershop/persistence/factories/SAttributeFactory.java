package org.yetanothershop.persistence.factories;

import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SAttributeType;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public interface SAttributeFactory
{
    SAttribute create(String name, SAttributeType type, SObjectType refObjType);
}