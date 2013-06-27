package org.yetanothershop.persistence.daos;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yetanothershop.persistence.AbstractSpringTest;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.entities.SObjectTypeImpl;

/**
 *
 */
public class SObjectTypeDaoTest extends AbstractSpringTest
{
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao objTypeDao;


    @Test
    public void findAll()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
        SObjectTypeImpl type2 = new SObjectTypeImpl("type2");
        objTypeDao.create(type1);
        List<SObjectType> allTypes = objTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1));
        objTypeDao.create(type2);
        allTypes = objTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1, type2));
    }
}