package org.yetanothershop.persistence.factories;

import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SAttributeType;

/**
 *
 */
public interface SAttributeFactory
{
    SAttribute create(String name, SAttributeType type);
}