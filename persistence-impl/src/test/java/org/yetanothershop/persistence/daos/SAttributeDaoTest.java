package org.yetanothershop.persistence.daos;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yetanothershop.persistence.AbstractSpringTest;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SAttributeImpl;
import org.yetanothershop.persistence.entities.SAttributeType;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.entities.SObjectTypeImpl;

/**
 *
 */
public class SAttributeDaoTest extends AbstractSpringTest
{
    @Resource(name = "sAttributeDao")
    private SAttributeDao attributeDao;
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao objTypeDao;


    @Test
    public void findByObjectType()
    {
        SObjectType objType = objTypeDao.createOrUpdate(new SObjectTypeImpl("objType1"));
        List<SAttribute> attrs = attributeDao.findByObjectType(objType.getId());
        Assert.assertTrue(attrs.isEmpty());
        SAttribute attr1 = attributeDao.createOrUpdate(new SAttributeImpl("attr1", SAttributeType.TEXT));
        attrs = attributeDao.findByObjectType(objType.getId());
        Assert.assertTrue(attrs.isEmpty());
        objType.addAttribute(attr1);
        attrs = attributeDao.findByObjectType(objType.getId());
        Assert.assertEquals(attrs, Arrays.asList(attr1));
    }
}
