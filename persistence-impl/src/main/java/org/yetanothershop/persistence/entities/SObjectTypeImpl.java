package org.yetanothershop.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 */
@Entity
@Table(name = "S_Object_Types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SObjectTypeImpl extends BaseEntity implements SObjectType
{
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = SAttributeImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<SAttribute> attributes = new ArrayList<SAttribute>();
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = SAttributeImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<SAttribute> staticAttributes = new ArrayList<SAttribute>();
    @CollectionTable(name = "S_Object_Types_Attr_Values")
    @ElementCollection(targetClass = SAttrValueImpl.class)
    private List<SAttrValue> staticAttrValues = new ArrayList<SAttrValue>();


    public SObjectTypeImpl()
    {
        this("");
    }


    public SObjectTypeImpl(String name)
    {
        this.name = name;
    }


    @Override
    public String getName()
    {
        return name;
    }


    @Override
    public List<SAttribute> getAssociatedAttrs()
    {
        return attributes;
    }


    @Override
    public void addAttribute(SAttribute attribute)
    {
        attributes.add(attribute);
    }


    @Override
    public List<SAttribute> getStaticAttrs()
    {
        return staticAttributes;
    }


    @Override
    public List<SAttrValue> getStaticAttrValues()
    {
        return staticAttrValues;
    }
}
