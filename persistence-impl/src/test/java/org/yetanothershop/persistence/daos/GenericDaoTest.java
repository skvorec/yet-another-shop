package org.yetanothershop.persistence.daos;

import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yetanothershop.persistence.AbstractSpringTest;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.entities.SObjectTypeImpl;

/**
 *
 */
public class GenericDaoTest extends AbstractSpringTest
{
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao objTypeDao;


//    @Test
//    public void createOrUpdate()
//    {
//        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
//        SObjectTypeImpl type2 = new SObjectTypeImpl("type2");
//        objTypeDao.createOrUpdate(type1);
//        Assert.assertEquals((Long) type1.getId(), new Long(1000));
//        Assert.assertEquals((Long) type2.getId(), new Long(1001));
//    }


    @Test
    public void findById()
    {
        SObjectType objType1 = objTypeDao.createOrUpdate(new SObjectTypeImpl("name1"));
        SObjectType found = objTypeDao.findById(objType1.getId());

        Assert.assertEquals(objType1, found);
    }
}