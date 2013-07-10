package org.yetanothershop.persistence.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 */
@Embeddable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SAttrValueImpl implements SAttrValue, Serializable
{
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SAttributeImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SAttribute attribute;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SObjectImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SObject refObject;
    private String attrValue;


    public SAttrValueImpl()
    {
        this(null, null);
    }


    public SAttrValueImpl(SAttribute attribute, String value)
    {
        this(attribute, null, value);
    }


    public SAttrValueImpl(SAttribute attribute, SObject refObjectValue, String value)
    {
        this.attribute = attribute;
        this.refObject = refObjectValue;
        this.attrValue = value;
    }


    @Override
    public SAttribute getAttribute()
    {
        return attribute;
    }


    @Override
    public String getAttrValue()
    {
        return attrValue;
    }


    @Override
    public SObject getRefValue()
    {
        return refObject;
    }
}