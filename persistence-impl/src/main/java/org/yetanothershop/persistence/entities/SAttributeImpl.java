package org.yetanothershop.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 */
@Entity
@Table(name = "S_Attributes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SAttributeImpl extends BaseEntity implements SAttribute
{
    private String name;
    private SAttributeType type;


    public SAttributeImpl()
    {
        this("", SAttributeType.TEXT);
    }


    public SAttributeImpl(String name, SAttributeType type)
    {
        this.name = name;
        this.type = type;
    }


    @Override
    public SAttributeType getType()
    {
        return type;
    }


    @Override
    public String getName()
    {
        return name;
    }
}