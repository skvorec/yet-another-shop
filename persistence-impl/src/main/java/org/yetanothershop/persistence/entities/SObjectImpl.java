package org.yetanothershop.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 */
@Entity
@Table(name = "S_Objects")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SObjectImpl extends BaseEntityImpl implements SObject
{
    @OneToOne(fetch = FetchType.LAZY, targetEntity = SObjectImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SObject parent;
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SObjectTypeImpl objectType;
    @CollectionTable(name = "S_Objects_Attr_Values")
    @ElementCollection(targetClass = SAttrValueImpl.class)
    private List<SAttrValue> attrValues = new ArrayList<SAttrValue>();


    public SObjectImpl()
    {
        this("", null, null);
    }


    public SObjectImpl(String name, SObjectImpl parent, SObjectTypeImpl objectType)
    {
        this.name = name;
        this.parent = parent;
        this.objectType = objectType;
    }


    @Override
    public SObject getParent()
    {
        return parent;
    }


    @Override
    public SObjectType getObjectType()
    {
        return objectType;
    }


    @Override
    public List<SAttrValue> getAttrValues()
    {
        return attrValues;
    }


    @Override
    public void addAttribureValue(SAttrValue value)
    {
        attrValues.add(value);
    }


    @Override
    public void removeAttributeValue(SAttrValue value)
    {
        attrValues.remove(value);
    }
}
