package org.yetanothershop.persistence.factories;

import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.entities.SObjectTypeImpl;

/**
 *
 */
public class SObjectTypeFactoryImpl implements SObjectTypeFactory
{
    @Override
    public SObjectType create(String name)
    {
        return new SObjectTypeImpl(name);
    }
}