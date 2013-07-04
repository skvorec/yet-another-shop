package org.yetanothershop.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 */
@Entity
@Table(name = "S_Attributes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NAME"})
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SAttributeImpl extends BaseEntityImpl implements SAttribute
{
    private SAttributeType type;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SObjectTypeImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private SObjectType refObjectType;


    public SAttributeImpl()
    {
        this("", SAttributeType.TEXT, null);
    }


    public SAttributeImpl(String name, SAttributeType type, SObjectType refObjectType)
    {
        this.name = name;
        this.type = type;
        this.refObjectType = refObjectType;
    }


    @Override
    public SAttributeType getType()
    {
        return type;
    }


    @Override
    public SObjectType getRefObjectType()
    {
        return refObjectType;
    }
}