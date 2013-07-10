package org.yetanothershop.persistence.factories;

import org.yetanothershop.persistence.entities.SAttrValue;
import org.yetanothershop.persistence.entities.SAttrValueImpl;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObject;

/**
 *
 */
public class SAttrValueFactoryImpl implements SAttrValueFactory
{
    @Override
    public SAttrValue create(SAttribute sAttribute, SObject referenceValue, String textValue)
    {
        return new SAttrValueImpl(sAttribute, referenceValue, textValue);
    }
}