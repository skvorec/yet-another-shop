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
import org.yetanothershop.web.controllers.commands.AttrCreationDto;
import org.yetanothershop.web.controllers.commands.NameAwareDto;

/**
 *
 */
@Controller
@Transactional(propagation = Propagation.REQUIRES_NEW)
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


    @RequestMapping(value = "/admin/attrManager", method = RequestMethod.GET)
    public String get(@RequestParam(value = "objtype", defaultValue = "none") String objTypeId,
            ModelMap modelMap)
    {
        List<SObjectType> allObjTypes = sObjectTypeDao.findAll();
        List<SAttribute> boundAttrs = new ArrayList<SAttribute>();
        if (!objTypeId.equals("none")) {
            boundAttrs = sAttributeDao.findByObjectType(Long.parseLong(objTypeId));
        }
        modelMap.addAttribute("objTypes", new ArrayList(allObjTypes));
        modelMap.addAttribute("attrs", boundAttrs);
        modelMap.addAttribute("NameAware", new NameAwareDto());
        AttrCreationDto attrCreationDto = new AttrCreationDto();
        attrCreationDto.setObjectTypeId(objTypeId);
        modelMap.addAttribute("AttrCreation", attrCreationDto);
        return "/attrManager";
    }


    @RequestMapping(value = "/admin/attrManager/newObjectType", method = RequestMethod.POST)
    public String createNewObjectType(@ModelAttribute(value = "NameAware") NameAwareDto nameAwareDto,
            BindingResult bindingResult)
    {
        SObjectType newObjType = sObjectTypeFactory.create(nameAwareDto.getName());
        sObjectTypeDao.createOrUpdate(newObjType);
        return "redirect:/admin/attrManager";
    }


    @RequestMapping(value = "/admin/attrManager/newAttribute", method = RequestMethod.POST)
    public String createNewAttribute(@ModelAttribute(value = "AttrCreation") AttrCreationDto attrCreationDto,
            BindingResult bindingResult)
    {
        SAttribute attr =
                sAttributeFactory.create(attrCreationDto.getName(),
                SAttributeType.valueOf(attrCreationDto.getAttrType()));
        sAttributeDao.createOrUpdate(attr);        
        SObjectType objType = sObjectTypeDao.findById(Long.parseLong(attrCreationDto.getObjectTypeId()));
        objType.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + attrCreationDto.getObjectTypeId();
    }
}
