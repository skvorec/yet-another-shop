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
    private SAttributeDao sAttributeDao;
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao sObjectTypeDao;


    @Test
    public void findAll()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
        SObjectTypeImpl type2 = new SObjectTypeImpl("type2");
        sObjectTypeDao.createOrUpdate(type1);
        List<SObjectType> allTypes = sObjectTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1));
        sObjectTypeDao.createOrUpdate(type2);
        allTypes = sObjectTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1, type2));
    }


    @Test
    public void update()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
        sObjectTypeDao.createOrUpdate(type1);

        SObjectType found = sObjectTypeDao.findById(type1.getId());
        SAttributeImpl attr1 = new SAttributeImpl("attr1", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr1);

        found.addAttribute(attr1);
        sObjectTypeDao.createOrUpdate(found);
    }


    @Test
    //we must be sure that unicode search works as well
    public void findByName()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("мойТип1");
        SObjectTypeImpl type2 = new SObjectTypeImpl("тип2");
        sObjectTypeDao.createOrUpdate(type1);
        sObjectTypeDao.createOrUpdate(type2);
        List<SObjectType> findByName = sObjectTypeDao.findByPartOfName("тип");
        Assert.assertEquals(findByName, Arrays.asList(type1, type2));
        findByName = sObjectTypeDao.findByPartOfName("тип2");
        Assert.assertEquals(findByName, Arrays.asList(type2));
        findByName = sObjectTypeDao.findByPartOfName("мойТип");
        Assert.assertEquals(findByName, Arrays.asList(type1));
    }


    @Test
    public void delete()
    {
        SAttributeImpl attr = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr);

        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        sObjectTypeDao.createOrUpdate(type);

        type.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(type);

        sObjectTypeDao.delete(type);

        Assert.assertNotNull(sAttributeDao.findById(attr.getId()));
        Assert.assertNull(sObjectTypeDao.findById(type.getId()));
    }


   
}
