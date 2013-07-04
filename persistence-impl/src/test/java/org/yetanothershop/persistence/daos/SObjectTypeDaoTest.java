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
    private SObjectTypeDao sObjTypeDao;


    @Test
    public void findAll()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
        SObjectTypeImpl type2 = new SObjectTypeImpl("type2");
        sObjTypeDao.createOrUpdate(type1);
        List<SObjectType> allTypes = sObjTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1));
        sObjTypeDao.createOrUpdate(type2);
        allTypes = sObjTypeDao.findAll();
        Assert.assertEquals(allTypes, Arrays.asList(type1, type2));
    }


    @Test
    public void update()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("type1");
        sObjTypeDao.createOrUpdate(type1);

        SObjectType found = sObjTypeDao.findById(type1.getId());
        SAttributeImpl attr1 = new SAttributeImpl("attr1", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr1);

        found.addAttribute(attr1);
        sObjTypeDao.createOrUpdate(found);
    }


    @Test
    //we must be sure that unicode search works as well
    public void findByName()
    {
        SObjectTypeImpl type1 = new SObjectTypeImpl("мойТип1");
        SObjectTypeImpl type2 = new SObjectTypeImpl("тип2");
        sObjTypeDao.createOrUpdate(type1);
        sObjTypeDao.createOrUpdate(type2);
        List<SObjectType> findByName = sObjTypeDao.findByPartOfName("тип");
        Assert.assertEquals(findByName, Arrays.asList(type1, type2));
        findByName = sObjTypeDao.findByPartOfName("тип2");
        Assert.assertEquals(findByName, Arrays.asList(type2));
        findByName = sObjTypeDao.findByPartOfName("мойТип");
        Assert.assertEquals(findByName, Arrays.asList(type1));
    }


    @Test
    public void delete()
    {
        SAttributeImpl attr = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr);

        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        sObjTypeDao.createOrUpdate(type);

        type.addAttribute(attr);
        sObjTypeDao.createOrUpdate(type);

        sObjTypeDao.delete(type);

        Assert.assertNotNull(sAttributeDao.findById(attr.getId()));
        Assert.assertNull(sObjTypeDao.findById(type.getId()));
    }


    @Test
    public void unbindAttr()
    {
        SAttributeImpl attr = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        sAttributeDao.createOrUpdate(attr);

        SObjectTypeImpl type = new SObjectTypeImpl("type1");
        sObjTypeDao.createOrUpdate(type);

        type.addAttribute(attr);
        sObjTypeDao.createOrUpdate(type);
        Assert.assertEquals(type.getAssociatedAttrs().size(), 1);

        type.unbindAttr(attr);
        sObjTypeDao.createOrUpdate(type);

        Assert.assertEquals(type.getAssociatedAttrs().size(), 0);
        Assert.assertNotNull(sAttributeDao.findById(attr.getId()));
    }
}
