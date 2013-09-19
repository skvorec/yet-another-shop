package org.yetanothershop.persistence.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class SObjectTypeImpl extends BaseEntityImpl implements SObjectType {

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

    public SObjectTypeImpl() {
        this("");
    }

    public SObjectTypeImpl(String name) {
        this.name = name;
    }

    @Override
    public List<SAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public void addAttribute(SAttribute attribute) {
        attributes.add(attribute);
    }

    @Override
    public void unbindAttribute(SAttribute attribute) {
        attributes.remove(attribute);
    }

    @Override
    public void addStaticAttribute(SAttribute attribute) {
        staticAttributes.add(attribute);
    }

    @Override
    public void unbindStaticAttribute(SAttribute attribute) {
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
    public void addStaticAttrValue(SAttrValue attrValue) throws InconsistentEntityException {
        final SAttribute associatedAttr = attrValue.getAttribute();
        if (!staticAttributes.contains(associatedAttr)) {
            throw new InconsistentEntityException("Object type " + name + " does not have static attribute "
                    + associatedAttr.getName());
        }
        SAttrValueImpl attrValueImpl = (SAttrValueImpl) attrValue;
        attrValueImpl.setOrderNumber(getMaxOrderNumber(associatedAttr) + 1);
        staticAttrValues.add(attrValueImpl);
    }

    private long getMaxOrderNumber(SAttribute attribute) {
        long maxNumber = 0;
        for (SAttrValue attrValue : staticAttrValues) {
            if (attrValue.getAttribute().equals(attribute) && attrValue.getOrderNumber() > maxNumber) {
                maxNumber = attrValue.getOrderNumber();
            }
        }
        return maxNumber;
    }

    @Override
    public List<SAttribute> getStaticAttributes() {
        return staticAttributes;
    }

    @Override
    public List<SAttrValue> getStaticAttrValues(SAttribute attribute) {
        List<SAttrValue> result = new ArrayList<SAttrValue>();
        for (SAttrValue sAttrValue : staticAttrValues) {
            if (sAttrValue.getAttribute().equals(attribute)) {
                result.add(sAttrValue);
            }
        }
        Collections.sort(result, new Comparator<SAttrValue>() {
            @Override
            public int compare(SAttrValue o1, SAttrValue o2) {
                final SAttribute attribute1 = o1.getAttribute();
                final SAttribute attribute2 = o2.getAttribute();
                if (!attribute1.equals(attribute2)) {
                    return Long.compare(attribute1.getId(), attribute2.getId());
                }
                return Long.compare(o1.getOrderNumber(), o2.getOrderNumber());
            }
        });
        return result;
    }

    @Override
    public SAttrValue getStaticAttrValue(SAttribute attribute, long orderNumber){
        for (SAttrValue attrValue : staticAttrValues) {
            if (!attrValue.getAttribute().equals(attribute)) {
                continue;
            }
            if(attrValue.getOrderNumber() == orderNumber){
                return attrValue;
            }
        }
        return null;
    }

    @Override
    public void deleteStaticAttrValue(SAttribute attr, long orderNumber) {
        Iterator<SAttrValue> iterator = staticAttrValues.iterator();
        while (iterator.hasNext()) {
            SAttrValue staticAttrValue = iterator.next();
            if (staticAttrValue.getAttribute().equals(attr)
                    && staticAttrValue.getOrderNumber() == orderNumber) {
                iterator.remove();
            }
        }
    }

    //attr value with order num = 1 is the top one
    @Override
    public void moveUpStaticAttrValue(SAttribute attr, long orderNumber) {
        long previousOrderNumber = -1;
        SAttrValueImpl attrValueToMove = null;
        SAttrValueImpl attrValueToSwap = null;
        for (SAttrValue attrValue : staticAttrValues) {
            if (!attrValue.getAttribute().equals(attr)) {
                continue;
            }
            final long currentOrderNum = attrValue.getOrderNumber();
            if (currentOrderNum == orderNumber) {
                attrValueToMove = (SAttrValueImpl) attrValue;
            }
            if (previousOrderNumber < currentOrderNum && currentOrderNum < orderNumber) {
                previousOrderNumber = currentOrderNum;
                attrValueToSwap = (SAttrValueImpl) attrValue;
            }
        }

        if (attrValueToMove != null && attrValueToSwap != null) {
            attrValueToMove.setOrderNumber(attrValueToSwap.getOrderNumber());
            attrValueToSwap.setOrderNumber(orderNumber);
        }
    }

    @Override
    public void moveDownStaticAttrValue(SAttribute attr, long orderNumber) {
        long nextOrderNumber = Long.MAX_VALUE;
        SAttrValueImpl attrValueToMove = null;
        SAttrValueImpl attrValueToSwap = null;
        for (SAttrValue attrValue : staticAttrValues) {
            if (!attrValue.getAttribute().equals(attr)) {
                continue;
            }
            final long currentOrderNum = attrValue.getOrderNumber();
            if (currentOrderNum == orderNumber) {
                attrValueToMove = (SAttrValueImpl) attrValue;
            }
            if (nextOrderNumber > currentOrderNum && currentOrderNum > orderNumber) {
                nextOrderNumber = currentOrderNum;
                attrValueToSwap = (SAttrValueImpl) attrValue;
            }
        }

        if (attrValueToMove != null && attrValueToSwap != null) {
            attrValueToMove.setOrderNumber(attrValueToSwap.getOrderNumber());
            attrValueToSwap.setOrderNumber(orderNumber);
        }
    }
}
