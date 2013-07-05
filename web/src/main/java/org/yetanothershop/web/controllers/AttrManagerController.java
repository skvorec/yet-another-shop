package org.yetanothershop.web.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SAttributeType;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.factories.SAttributeFactory;
import org.yetanothershop.persistence.factories.SObjectTypeFactory;

/**
 *
 */
@Controller
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping(value = "/admin/attrManager")
@SessionAttributes("creatingError")
public class AttrManagerController
{
    private SObjectTypeFactory sObjectTypeFactory;
    private SObjectTypeDao sObjectTypeDao;
    private SAttributeDao sAttributeDao;
    private SAttributeFactory sAttributeFactory;


    public void setsObjectTypeFactory(SObjectTypeFactory sObjectTypeFactory)
    {
        this.sObjectTypeFactory = sObjectTypeFactory;
    }


    public void setsObjectTypeDao(SObjectTypeDao sObjectTypeDao)
    {
        this.sObjectTypeDao = sObjectTypeDao;
    }


    public void setsAttributeDao(SAttributeDao sAttributeDao)
    {
        this.sAttributeDao = sAttributeDao;
    }


    public void setsAttributeFactory(SAttributeFactory sAttributeFactory)
    {
        this.sAttributeFactory = sAttributeFactory;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String get(@RequestParam(value = "objtype", defaultValue = "none") String objTypeId,
            ModelMap modelMap)
    {
        List<SObjectType> allObjTypes = sObjectTypeDao.findAll();
        List<SAttribute> boundAttrs = new ArrayList<SAttribute>();
        List<SAttribute> staticAttrs = new ArrayList<SAttribute>();
        SObjectType currentObjType = null;
        if (!objTypeId.equals("none")) {
            final long otId = Long.parseLong(objTypeId);
            currentObjType = sObjectTypeDao.findById(otId);
            boundAttrs = currentObjType.getAssociatedAttrs();
            staticAttrs = currentObjType.getStaticAttrs();
        }
        modelMap.addAttribute("currentObjType", currentObjType);
        modelMap.addAttribute("objTypes", allObjTypes);
        modelMap.addAttribute("attrs", boundAttrs);
        modelMap.addAttribute("staticAttrs", staticAttrs);
        return "/attrManager";
    }


    @RequestMapping(value = "/newObjectType", method = RequestMethod.POST)
    public String createNewObjectType(ModelMap modelMap,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "previousObjType", defaultValue = "none") String previousObjType)
    {
        System.out.println("label0");
        try {
            System.out.println("label1");
            SObjectType newObjType = sObjectTypeFactory.create(name);
            sObjectTypeDao.createOrUpdate(newObjType);
            return "forward:/admin/attrManager?objtype=" + newObjType.getId();
        } catch (Exception ignore) {
            System.out.println("label2");
            modelMap.addAttribute("creatingError", "ObjectTypeCreatingError");
            return "forward:/admin/attrManager?objtype=" + previousObjType;
        }
    }


    @RequestMapping(value = "/newAttribute", method = RequestMethod.POST)
    public String createNewAttribute(
            @RequestParam(value = "attrName") String attrName,
            @RequestParam(value = "attrType") String attrType,
            @RequestParam(value = "objectTypeId") Long objectTypeId,
            @RequestParam(value = "refObjTypeId") Long refObjTypeId)
    {
        SAttributeType attributeType;
        try {
            attributeType = SAttributeType.valueOf(attrType);
        } catch (IllegalArgumentException ignore) {
            return "forward:/admin/attrManager?objtype=" + objectTypeId;
        }
        SObjectType refType = null;
        if (attributeType == SAttributeType.REFERENCE) {
            refType = sObjectTypeDao.findById(refObjTypeId);
        }
        SAttribute attr = sAttributeFactory.create(attrName, attributeType, refType);
        sAttributeDao.createOrUpdate(attr);
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        objType.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);
        return "forward:/admin/attrManager?objtype=" + objectTypeId;
    }


    @RequestMapping(value = "/addAttribute", method = RequestMethod.POST)
    public String addAttribute(
            @RequestParam(value = "attr") Long attrId,
            @RequestParam(value = "objtype") Long objectTypeId)
    {
        SAttribute attr = sAttributeDao.findById(attrId);
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        objType.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);
        return "forward:/admin/attrManager?objtype=" + objectTypeId;
    }


    @RequestMapping(value = "/deleteObjType", method = RequestMethod.GET)
    public String deleteObjType(@RequestParam(value = "objtype") Long objectTypeId)
    {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        sObjectTypeDao.delete(objType);
        return "forward:/admin/attrManager";
    }


    @RequestMapping(value = "/unbindAttr", method = RequestMethod.GET)
    public String unbindAttr(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId)
    {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.unbindAttr(attr);
        sObjectTypeDao.createOrUpdate(objType);

        return "forward:/admin/attrManager?objtype=" + objectTypeId;
    }


    @RequestMapping(value = "/unbindStaticAttr", method = RequestMethod.GET)
    public String unbindStaticAttr(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId)
    {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.unbindStaticAttr(attr);
        sObjectTypeDao.createOrUpdate(objType);

        return "forward:/admin/attrManager?objtype=" + objectTypeId;
    }
}
