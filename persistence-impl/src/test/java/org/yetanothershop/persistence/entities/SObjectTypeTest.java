package org.yetanothershop.persistence.entities;

import java.util.Arrays;
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
public class SObjectTypeTest extends AbstractSpringTest {

    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao sObjectTypeDao;
    @Resource(name = "sAttributeDao")
    private SAttributeDao sAttributeDao;

    @Test(expectedExceptions = InconsistentEntityException.class)
    public void addStaticAttributeValueException() throws InconsistentEntityException {
        SObjectTypeImpl objType = new SObjectTypeImpl("name1");
        final SAttributeImpl attr1 = new SAttributeImpl("attrName", SAttributeType.TEXT, null);
        objType.addStaticAttrValue(new SAttrValueImpl(attr1, null));
    }

    @Test
    public void addStaticAttributeValue() throws InconsistentEntityException {
        SObjectTypeImpl objType = new SObjectTypeImpl("name1");
        final SAttributeImpl attr1 = new SAttributeImpl("attrName1", SAttributeType.TEXT, null);
        objType.addStaticAttribute(attr1);
        objType.addStaticAttrValue(new SAttrValueImpl(attr1, "value1"));
        List<SAttrValue> attr1Values = objType.getStaticAttrValues(attr1);
        Assert.assertEquals(attr1Values.size(), 1);
        Assert.assertEquals(attr1Values.get(0).getOrderNumber(), 1);
        Assert.assertEquals(attr1Values.get(0).getAttrValue(), "value1");
    }

    @Test
    public void getStaticAttrValues() throws InconsistentEntityException {
        SAttributeImpl attr1 = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        SAttributeImpl attr2 = new SAttributeImpl("Attr2", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr1);
        sAttributeDao.createOrUpdate(attr2);
        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        type.addStaticAttribute(attr1);
        type.addStaticAttribute(attr2);
        type.addStaticAttrValue(new SAttrValueImpl(attr1, "text1"));
        type.addStaticAttrValue(new SAttrValueImpl(attr1, "text2"));
        type.addStaticAttrValue(new SAttrValueImpl(attr2, "text3"));
        sObjectTypeDao.createOrUpdate(type);
        SAttribute attr1Clone = sAttributeDao.findById(attr1.getId());
        List<SAttrValue> attr1Values = type.getStaticAttrValues(attr1Clone);
        Assert.assertEquals(attr1Values.size(), 2);
        Assert.assertEquals(attr1Values.get(0).getAttrValue(), "text1");
        Assert.assertEquals(attr1Values.get(1).getAttrValue(), "text2");
    }

    @Test
    public void unbindAttr() {
        SAttributeImpl attr = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr);

        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        sObjectTypeDao.createOrUpdate(type);

        type.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(type);
        Assert.assertEquals(type.getAttributes().size(), 1);

        type.unbindAttribute(attr);
        sObjectTypeDao.createOrUpdate(type);

        Assert.assertEquals(type.getAttributes().size(), 0);
        Assert.assertNotNull(sAttributeDao.findById(attr.getId()));
    }

    @Test
    public void unbindStaticAttr() throws InconsistentEntityException {
        SAttributeImpl attr1 = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        SAttributeImpl attr2 = new SAttributeImpl("Attr2", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr1);
        sAttributeDao.createOrUpdate(attr2);
        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        type.addStaticAttribute(attr1);
        type.addStaticAttribute(attr2);
        type.addStaticAttrValue(new SAttrValueImpl(attr1, "text1"));
        type.addStaticAttrValue(new SAttrValueImpl(attr1, "text2"));
        type.addStaticAttrValue(new SAttrValueImpl(attr2, "text3"));
        sObjectTypeDao.createOrUpdate(type);
        Assert.assertEquals(type.staticAttrValues.size(), 3);
        type.unbindStaticAttribute(attr1);
        Assert.assertEquals(type.staticAttrValues.size(), 1);
    }

    @Test
    public void deleteStaticAttrValue() throws InconsistentEntityException {
        SObjectTypeImpl objType = new SObjectTypeImpl("name1");
        final SAttributeImpl attr1 = new SAttributeImpl("attrName1", SAttributeType.TEXT, null);
        objType.addStaticAttribute(attr1);
        final SAttrValueImpl attrValue1 = new SAttrValueImpl(attr1, "value1");
        final SAttrValueImpl attrValue2 = new SAttrValueImpl(attr1, "value2");
        objType.addStaticAttrValue(attrValue1);
        objType.addStaticAttrValue(attrValue2);

        Assert.assertEquals(objType.getStaticAttrValues(attr1).size(), 2);
        objType.deleteStaticAttrValue(attr1, 1);
        Assert.assertEquals(objType.getStaticAttrValues(attr1).size(), 1);
        objType.deleteStaticAttrValue(attr1, 2);
        Assert.assertEquals(objType.getStaticAttrValues(attr1).size(), 0);
    }

    @Test
    public void moveUpStaticAttrValue() throws InconsistentEntityException {
        SObjectTypeImpl objType = new SObjectTypeImpl("name1");
        final SAttributeImpl attr1 = new SAttributeImpl("attrName1", SAttributeType.TEXT, null);
        objType.addStaticAttribute(attr1);
        final SAttrValueImpl attrValue1 = new SAttrValueImpl(attr1, "value1");
        final SAttrValueImpl attrValue2 = new SAttrValueImpl(attr1, "value2");
        objType.addStaticAttrValue(attrValue1);
        objType.addStaticAttrValue(attrValue2);

        Assert.assertEquals(objType.getStaticAttrValues(attr1), Arrays.asList(attrValue1, attrValue2));
        objType.moveUpStaticAttrValue(attr1, 1);
        Assert.assertEquals(objType.getStaticAttrValues(attr1), Arrays.asList(attrValue1, attrValue2));
        objType.moveUpStaticAttrValue(attr1, 2);
        Assert.assertEquals(objType.getStaticAttrValues(attr1), Arrays.asList(attrValue2, attrValue1));
    }

    @Test
    public void moveDownStaticAttrValue() throws InconsistentEntityException {
        SObjectTypeImpl objType = new SObjectTypeImpl("name1");
        final SAttributeImpl attr1 = new SAttributeImpl("attrName1", SAttributeType.TEXT, null);
        objType.addStaticAttribute(attr1);
        final SAttrValueImpl attrValue1 = new SAttrValueImpl(attr1, "value1");
        final SAttrValueImpl attrValue2 = new SAttrValueImpl(attr1, "value2");
        objType.addStaticAttrValue(attrValue1);
        objType.addStaticAttrValue(attrValue2);

        Assert.assertEquals(objType.getStaticAttrValues(attr1), Arrays.asList(attrValue1, attrValue2));
        objType.moveDownStaticAttrValue(attr1, 2);
        Assert.assertEquals(objType.getStaticAttrValues(attr1), Arrays.asList(attrValue1, attrValue2));
        objType.moveDownStaticAttrValue(attr1, 1);
        Assert.assertEquals(objType.getStaticAttrValues(attr1), Arrays.asList(attrValue2, attrValue1));
    }

    @Test
    public void getStaticAttrValue() throws InconsistentEntityException {
        SObjectTypeImpl objType = new SObjectTypeImpl("name1");
        final SAttributeImpl attr1 = new SAttributeImpl("attrName1", SAttributeType.TEXT, null);
        objType.addStaticAttribute(attr1);
        final SAttrValueImpl attrValue1 = new SAttrValueImpl(attr1, "value1");
        final SAttrValueImpl attrValue2 = new SAttrValueImpl(attr1, "value2");
        objType.addStaticAttrValue(attrValue1);
        objType.addStaticAttrValue(attrValue2);
        Assert.assertEquals(objType.getStaticAttrValue(attr1, 1), attrValue1);
        Assert.assertEquals(objType.getStaticAttrValue(attr1, 2), attrValue2);
        objType.moveDownStaticAttrValue(attr1, 1);
        Assert.assertEquals(objType.getStaticAttrValue(attr1, 1), attrValue2);
        objType.deleteStaticAttrValue(attr1, 1);
        Assert.assertEquals(objType.getStaticAttrValue(attr1, 2), attrValue1);
    }
}