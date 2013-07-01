package org.yetanothershop.persistence.daos;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yetanothershop.persistence.AbstractSpringTest;
import org.yetanothershop.persistence.entities.SAttributeImpl;
import org.yetanothershop.persistence.entities.SAttributeType;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.entities.SObjectTypeImpl;

/**
 *
 */
public class SObjectTypeDaoTest extends AbstractSpringTest
{
    @Resource(name = "sAttributeDao")
    private SAttributeDao attributeDao;
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao objTypeDao;


    @Test
    public void findAll()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
        SObjectTypeImpl type2 = new SObjectTypeImpl("type2");
        objTypeDao.createOrUpdate(type1);
        List<SObjectType> allTypes = objTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1));
        objTypeDao.createOrUpdate(type2);
        allTypes = objTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1, type2));
    }


    @Test
    public void update()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
        objTypeDao.createOrUpdate(type1);

        SObjectType found = objTypeDao.findById(type1.getId());
        SAttributeImpl attr1 = new SAttributeImpl("attr1", SAttributeType.TEXT);
        attributeDao.createOrUpdate(attr1);

        found.addAttribute(attr1);
        objTypeDao.createOrUpdate(found);
    }
}