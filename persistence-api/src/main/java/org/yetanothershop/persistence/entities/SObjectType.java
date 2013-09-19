package org.yetanothershop.persistence.entities;

import java.util.List;

/**
 *
 */
public interface SObjectType extends BaseEntity {

    List<SAttribute> getAttributes();

    List<SAttribute> getStaticAttributes();

    List<SAttrValue> getStaticAttrValues(SAttribute attribute);

    SAttrValue getStaticAttrValue(SAttribute attribute, long orderNumber);

    void addAttribute(SAttribute attribute);

    void unbindAttribute(SAttribute attribute);

    void addStaticAttribute(SAttribute attribute);

    void unbindStaticAttribute(SAttribute attribute);

    void addStaticAttrValue(SAttrValue attrValue) throws InconsistentEntityException;

    void deleteStaticAttrValue(SAttribute attr, long orderNumber);

    void moveUpStaticAttrValue(SAttribute attr, long orderNumber);

    void moveDownStaticAttrValue(SAttribute attr, long orderNumber);
}
