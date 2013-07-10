package org.yetanothershop.persistence.daos;

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
public class GenericDaoTest extends AbstractSpringTest
{
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao sObjectTypeDao;


    @Test
    public void findById()
    {
        SObjectType objType1 = sObjectTypeDao.createOrUpdate(new SObjectTypeImpl("name1"));
        SObjectType found = sObjectTypeDao.findById(objType1.getId());

        Assert.assertEquals(objType1, found);
    }


    @Test
    public void mergeTest()
    {
        SObjectTypeImpl ot1 = new SObjectTypeImpl("name1");
        SObjectTypeImpl ot2 = new SObjectTypeImpl("name2");
        ot1.setId(new Long(1000));
        sObjectTypeDao.merge(ot1);
        sObjectTypeDao.createOrUpdate(ot2);
        SObjectType findById = sObjectTypeDao.findById(new Long(1000));
        Assert.assertEquals(findById.getName(), "name1");
        findById = sObjectTypeDao.findByName("name2");
        Assert.assertTrue((0 < findById.getId()) && (findById.getId() < 1000l));
    }


    //TODO: make this test failure!
    @Test
    public void createUniqueName()
    {
        System.out.println("----------------------");
        SObjectTypeImpl ot1 = new SObjectTypeImpl("name2");
        SObjectTypeImpl ot2 = new SObjectTypeImpl("name2");

        sObjectTypeDao.createOrUpdate(ot1);
        sObjectTypeDao.createOrUpdate(ot2);
        List<SObjectType> findAll = sObjectTypeDao.findAll();
        for (SObjectType objType : findAll) {
            System.out.println("id: " + objType.getId());
            System.out.println("name: " + objType.getName());
        }
    }
}
