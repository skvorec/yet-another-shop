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


    @Test
    public void findById()
    {
        SObjectType objType1 = objTypeDao.createOrUpdate(new SObjectTypeImpl("name1"));
        SObjectType found = objTypeDao.findById(objType1.getId());

        Assert.assertEquals(objType1, found);
    }
}