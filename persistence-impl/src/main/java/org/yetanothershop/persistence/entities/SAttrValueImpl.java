package org.yetanothershop.persistence.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
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
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SAttributeImpl attribute;
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SObjectTypeImpl refObjType;
    private String attrValue;


    public SAttrValueImpl()
    {
        this(null, null);
    }


    public SAttrValueImpl(SAttributeImpl attribute, String value)
    {
        this(attribute, null, value);
    }


    public SAttrValueImpl(SAttributeImpl attribute, SObjectTypeImpl refObjType, String value)
    {
        this.attribute = attribute;
        this.refObjType = refObjType;
        this.attrValue = value;
    }


    public SAttribute getAttribute()
    {
        return attribute;
    }


    public SObjectType getRefObjectType()
    {
        return refObjType;
    }


    public String getAttrValue()
    {
        return attrValue;
    }
}