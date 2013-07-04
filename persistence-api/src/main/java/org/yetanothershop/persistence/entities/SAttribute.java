package org.yetanothershop.persistence.entities;

/**
 *
 */
public interface SAttribute extends BaseEntity
{
    SAttributeType getType();


    SObjectType getRefObjectType();
}