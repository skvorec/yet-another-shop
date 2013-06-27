package org.yetanothershop.persistence.factories;

import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public interface SObjectTypeFactory
{
    SObjectType create(String name);
}