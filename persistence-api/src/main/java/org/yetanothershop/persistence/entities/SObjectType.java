package org.yetanothershop.persistence.entities;

import java.util.List;

/**
 *
 */
public interface SObjectType extends Identifiable
{
    String getName();


    List<SAttribute> getAssociatedAttrs();


    List<SAttribute> getStaticAttrs();


    List<SAttrValue> getStaticAttrValues();


    void addAttribute(SAttribute attribute);
}