package org.yetanothershop.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.factories.SObjectTypeFactory;

/**
 *
 */
@Controller
public class AdminPageController
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


    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String get()
    {
        SObjectType objType = sObjectTypeFactory.create("newObjType");
        sObjectTypeDao.create(objType);
        return "/admin";
    }
}