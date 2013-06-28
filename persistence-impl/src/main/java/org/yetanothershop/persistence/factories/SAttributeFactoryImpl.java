package org.yetanothershop.persistence.factories;

import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SAttributeImpl;
import org.yetanothershop.persistence.entities.SAttributeType;

/**
 *
 */
public class SAttributeFactoryImpl implements SAttributeFactory
{
    @Override
    public SAttribute create(String name, SAttributeType type)
    {
        return new SAttributeImpl(name, type);
    }
}