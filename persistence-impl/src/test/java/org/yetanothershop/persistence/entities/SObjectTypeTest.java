package org.yetanothershop.persistence.entities;

import java.util.List;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yetanothershop.persistence.AbstractSpringTest;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;

/**
 *
 */
public class SObjectTypeTest extends AbstractSpringTest
{
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao sObjectTypeDao;
    @Resource(name = "sAttributeDao")
    private SAttributeDao sAttributeDao;


    @Test(expectedExceptions = InconsistentEntityException.class)
    public void addStaticAttributeValue() throws InconsistentEntityException
    {
        SObjectTypeImpl objType = new SObjectTypeImpl("name1");
        final SAttributeImpl attr1 = new SAttributeImpl("attrName", SAttributeType.TEXT, null);
        objType.addStaticAttributeValue(new SAttrValueImpl(attr1, null));
    }


    @Test
    public void getStaticAttrValues() throws InconsistentEntityException
    {
        SAttributeImpl attr1 = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        SAttributeImpl attr2 = new SAttributeImpl("Attr2", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr1);
        sAttributeDao.createOrUpdate(attr2);
        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        type.addStaticAttribute(attr1);
        type.addStaticAttribute(attr2);
        type.addStaticAttributeValue(new SAttrValueImpl(attr1, "text1"));
        type.addStaticAttributeValue(new SAttrValueImpl(attr1, "text2"));
        type.addStaticAttributeValue(new SAttrValueImpl(attr2, "text3"));
        sObjectTypeDao.createOrUpdate(type);
        SAttribute attr1Clone = sAttributeDao.findById(attr1.getId());
        List<SAttrValue> attr1Values = type.getStaticAttrValues(attr1Clone);
        Assert.assertEquals(attr1Values.size(), 2);
        Assert.assertEquals(attr1Values.get(0).getAttrValue(), "text1");
        Assert.assertEquals(attr1Values.get(1).getAttrValue(), "text2");
    }


    @Test
    public void unbindAttr()
    {
        SAttributeImpl attr = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr);

        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        sObjectTypeDao.createOrUpdate(type);

        type.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(type);
        Assert.assertEquals(type.getAssociatedAttrs().size(), 1);

        type.unbindAttr(attr);
        sObjectTypeDao.createOrUpdate(type);

        Assert.assertEquals(type.getAssociatedAttrs().size(), 0);
        Assert.assertNotNull(sAttributeDao.findById(attr.getId()));
    }


    @Test
    public void unbindStaticAttr() throws InconsistentEntityException
    {
        SAttributeImpl attr1 = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        SAttributeImpl attr2 = new SAttributeImpl("Attr2", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr1);
        sAttributeDao.createOrUpdate(attr2);
        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        type.addStaticAttribute(attr1);
        type.addStaticAttribute(attr2);
        type.addStaticAttributeValue(new SAttrValueImpl(attr1, "text1"));
        type.addStaticAttributeValue(new SAttrValueImpl(attr1, "text2"));
        type.addStaticAttributeValue(new SAttrValueImpl(attr2, "text3"));
        sObjectTypeDao.createOrUpdate(type);
        Assert.assertEquals(type.staticAttrValues.size(), 3);
        type.unbindStaticAttr(attr1);
        Assert.assertEquals(type.staticAttrValues.size(), 1);
    }
}