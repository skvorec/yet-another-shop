package org.yetanothershop.persistence.entities;

import java.util.List;

/**
 *
 */
public interface SObjectType extends BaseEntity
{
    List<SAttribute> getAssociatedAttrs();


    List<SAttribute> getStaticAttrs();


    List<SAttrValue> getStaticAttrValues(SAttribute attribute);


    void addAttribute(SAttribute attribute);


    void addStaticAttribute(SAttribute attribute);


    void addStaticAttributeValue(SAttrValue attrValue) throws InconsistentEntityException;


    void unbindAttr(SAttribute attribute);


    void unbindStaticAttr(SAttribute attribute);
}
