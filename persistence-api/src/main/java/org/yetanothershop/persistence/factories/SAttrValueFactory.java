package org.yetanothershop.persistence.factories;

import org.yetanothershop.persistence.entities.SAttrValue;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObject;

/**
 *
 */
public interface SAttrValueFactory
{
    SAttrValue create(SAttribute sAttribute, SObject referenceValue, String textValue);
}