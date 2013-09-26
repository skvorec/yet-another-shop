package org.yetanothershop.persistence.entities;

import java.util.List;

/**
 *
 */
public interface SObject extends BaseEntity
{
    SObject getParent();

    SObjectType getObjectType();

    List<SAttrValue> getAttrValues(SAttribute attr);

    void addAttribureValue(SAttrValue value);

    void removeAttributeValue(SAttrValue value);
}
