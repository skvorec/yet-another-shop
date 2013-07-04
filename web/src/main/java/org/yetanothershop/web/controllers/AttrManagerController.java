package org.yetanothershop.web.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SAttributeType;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.factories.SAttributeFactory;
import org.yetanothershop.persistence.factories.SObjectTypeFactory;
import org.yetanothershop.web.controllers.commands.NameAwareDto;

/**
 *
 */
@Controller
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping(value = "/admin/attrManager")
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
        SObjectType currentObjType = null;
        if (!objTypeId.equals("none")) {
            final long otId = Long.parseLong(objTypeId);
            boundAttrs = sAttributeDao.findByObjectType(otId);
            currentObjType = sObjectTypeDao.findById(otId);
        }
        modelMap.addAttribute("currentObjType", currentObjType);
        modelMap.addAttribute("objTypes", allObjTypes);
        modelMap.addAttribute("attrs", boundAttrs);
        modelMap.addAttribute("NameAware", new NameAwareDto());
        return "/attrManager";
    }


    @RequestMapping(value = "/newObjectType", method = RequestMethod.POST)
    public String createNewObjectType(@ModelAttribute(value = "NameAware") NameAwareDto nameAwareDto,
            BindingResult bindingResult)
    {
        SObjectType newObjType = sObjectTypeFactory.create(nameAwareDto.getName());
        sObjectTypeDao.createOrUpdate(newObjType);
        return "redirect:/admin/attrManager?objtype=" + newObjType.getId();
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
            return "redirect:/admin/attrManager?objtype=" + objectTypeId;
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
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }


    @RequestMapping(value = "/addAttribute", method = RequestMethod.POST)
    public String addAttribute(
            @RequestParam(value = "attrId") Long attrId,
            @RequestParam(value = "objectTypeId") Long objectTypeId)
    {
        SAttribute attr = sAttributeDao.findById(attrId);
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        objType.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }
}
