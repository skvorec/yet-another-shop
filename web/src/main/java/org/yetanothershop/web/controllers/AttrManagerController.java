package org.yetanothershop.web.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.factories.SObjectTypeFactory;
import org.yetanothershop.web.controllers.commands.NameAwareDto;

/**
 *
 */
@Controller
public class AttrManagerController
{
    private SObjectTypeFactory sObjectTypeFactory;
    private SObjectTypeDao sObjectTypeDao;


    public void setsObjectTypeFactory(SObjectTypeFactory sObjectTypeFactory)
    {
        this.sObjectTypeFactory = sObjectTypeFactory;
    }


    public void setsObjectTypeDao(SObjectTypeDao sObjectTypeDao)
    {
        this.sObjectTypeDao = sObjectTypeDao;
    }


    @RequestMapping(value = "/admin/attrManager", method = RequestMethod.GET)
    public String getWithObjType(@RequestParam(value = "objtype", defaultValue = "none") String objType,
            ModelMap modelMap)
    {
        List<SObjectType> allObjTypes = sObjectTypeDao.findAll();
        modelMap.addAttribute("ObjTypes", new ArrayList(allObjTypes));
        modelMap.addAttribute("NameAware", new NameAwareDto());
        return "/attrManager";
    }


    @RequestMapping(value = "/admin/attrManager/newObjectType", method = RequestMethod.POST)
    public String createNewObjectType(@ModelAttribute(value = "NameAware") NameAwareDto nameAwareDto,
            BindingResult bindingResult)
    {
        SObjectType newObjType = sObjectTypeFactory.create(nameAwareDto.getName());
        sObjectTypeDao.create(newObjType);
        return "redirect:/admin/attrManager";
    }
}