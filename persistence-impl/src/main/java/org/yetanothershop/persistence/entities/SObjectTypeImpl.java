package org.yetanothershop.persistence.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "S_Object_Types", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NAME"})
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SObjectTypeImpl extends BaseEntityImpl implements SObjectType
{
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = SAttributeImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "S_ObjectType_SAttribute",
    joinColumns = {
        @JoinColumn(name = "ObjectType_ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "Attribute_ID")},
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ObjectType_ID", "Attribute_ID"})
    })
    private List<SAttribute> attributes = new ArrayList<SAttribute>();
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = SAttributeImpl.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "S_ObjectType_Static_SAttribute",
    joinColumns = {
        @JoinColumn(name = "ObjectType_ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "Attribute_ID")},
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ObjectType_ID", "Attribute_ID"})
    })
    private List<SAttribute> staticAttributes = new ArrayList<SAttribute>();
    @CollectionTable(name = "S_Object_Types_Attr_Values")
    @ElementCollection(targetClass = SAttrValueImpl.class)
    protected List<SAttrValue> staticAttrValues = new ArrayList<SAttrValue>();


    public SObjectTypeImpl()
    {
        this("");
    }


    public SObjectTypeImpl(String name)
    {
        this.name = name;
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
    public void addStaticAttribute(SAttribute attribute)
    {
        staticAttributes.add(attribute);
    }


    @Override
    public void addStaticAttributeValue(SAttrValue attrValue) throws InconsistentEntityException
    {
        if (!staticAttributes.contains(attrValue.getAttribute())) {
            throw new InconsistentEntityException("Object type " + name + " does not have static attribute "
                    + attrValue.getAttribute().getName());
        }
        staticAttrValues.add(attrValue);
    }


    @Override
    public void unbindAttr(SAttribute attribute)
    {
        attributes.remove(attribute);
    }


    @Override
    public void unbindStaticAttr(SAttribute attribute)
    {
        staticAttributes.remove(attribute);
        Iterator<SAttrValue> iterator = staticAttrValues.iterator();
        while (iterator.hasNext()) {
            SAttrValue staticAttrValue = iterator.next();
            if (staticAttrValue.getAttribute().equals(attribute)) {
                iterator.remove();
            }
        }
    }


    @Override
    public List<SAttribute> getStaticAttrs()
    {
        return staticAttributes;
    }


    @Override
    public List<SAttrValue> getStaticAttrValues(SAttribute attribute)
    {
        List<SAttrValue> result = new ArrayList<SAttrValue>();
        for (SAttrValue sAttrValue : staticAttrValues) {
            if (sAttrValue.getAttribute().equals(attribute)) {
                result.add(sAttrValue);
            }
        }
        return result;
    }
}
