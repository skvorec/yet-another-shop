package org.yetanothershop.web.controllers;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.InconsistentEntityException;
import org.yetanothershop.persistence.entities.SAttrValue;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SAttributeType;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.persistence.factories.SAttrValueFactory;
import org.yetanothershop.persistence.factories.SAttributeFactory;
import org.yetanothershop.persistence.factories.SObjectTypeFactory;

/**
 *
 */
@Controller
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping(value = "/admin/attrManager")
@SessionAttributes("creatingError")
public class AttrManagerController {

    private SObjectTypeFactory sObjectTypeFactory;
    private SObjectTypeDao sObjectTypeDao;
    private SAttributeDao sAttributeDao;
    private SAttributeFactory sAttributeFactory;
    private SAttrValueFactory sAttrValueFactory;

    public void setsObjectTypeFactory(SObjectTypeFactory sObjectTypeFactory) {
        this.sObjectTypeFactory = sObjectTypeFactory;
    }

    public void setsObjectTypeDao(SObjectTypeDao sObjectTypeDao) {
        this.sObjectTypeDao = sObjectTypeDao;
    }

    public void setsAttributeDao(SAttributeDao sAttributeDao) {
        this.sAttributeDao = sAttributeDao;
    }

    public void setsAttributeFactory(SAttributeFactory sAttributeFactory) {
        this.sAttributeFactory = sAttributeFactory;
    }

    public void setsAttrValueFactory(SAttrValueFactory sAttrValueFactory) {
        this.sAttrValueFactory = sAttrValueFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(@RequestParam(value = "objtype", defaultValue = "none") String objTypeId,
            ModelMap modelMap) {
        List<SObjectType> allObjTypes = sObjectTypeDao.findAll();
        List<SAttribute> boundAttrs = new ArrayList<SAttribute>();
        List<SAttribute> staticAttrs = new ArrayList<SAttribute>();
        SObjectType currentObjType = null;
        if (!objTypeId.equals("none")) {
            final long otId = Long.parseLong(objTypeId);
            currentObjType = sObjectTypeDao.findById(otId);
            boundAttrs = currentObjType.getAttributes();
            staticAttrs = currentObjType.getStaticAttributes();
        }
        modelMap.addAttribute("currentObjType", currentObjType);
        modelMap.addAttribute("objTypes", allObjTypes);
        modelMap.addAttribute("attrs", boundAttrs);
        modelMap.addAttribute("staticAttrs", staticAttrs);
        return "/attrManager";
    }

    @ResponseBody
    @RequestMapping(value = "/newObjectType", method = RequestMethod.POST)
    public String createNewObjectType(@RequestParam(value = "name") String name) throws JSONException {

        SObjectType newObjType = sObjectTypeFactory.create(name);
        sObjectTypeDao.createOrUpdate(newObjType);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(newObjType.getId().toString(), name);
        return jsonObj.toString(1);

    }

    @ResponseBody
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public String canntCreateException() {
        return "{}";
    }

    @RequestMapping(value = "/newAttribute", method = RequestMethod.POST)
    public String newAttribute(
            @RequestParam(value = "attrName") String attrName,
            @RequestParam(value = "attrType") String attrType,
            @RequestParam(value = "objectTypeId") Long objectTypeId,
            @RequestParam(value = "refObjTypeId") Long refObjTypeId) {
        SAttribute newAttr;
        try {
            newAttr = createNewAttribute(attrName, attrType, refObjTypeId);
        } catch (IllegalArgumentException ignore) {
            return "redirect:/admin/attrManager?objtype=" + objectTypeId;
        }
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        objType.addAttribute(newAttr);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/newStaticAttribute", method = RequestMethod.POST)
    public String newStaticAttribute(
            @RequestParam(value = "attrName") String attrName,
            @RequestParam(value = "attrType") String attrType,
            @RequestParam(value = "objectTypeId") Long objectTypeId,
            @RequestParam(value = "refObjTypeId") Long refObjTypeId) {
        SAttribute newAttr;
        try {
            newAttr = createNewAttribute(attrName, attrType, refObjTypeId);
        } catch (IllegalArgumentException ignore) {
            return "redirect:/admin/attrManager?objtype=" + objectTypeId;
        }

        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        objType.addStaticAttribute(newAttr);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    private SAttribute createNewAttribute(String attrName, String attrType, Long refObjTypeId) {
        SAttributeType attributeType = SAttributeType.valueOf(attrType);
        SObjectType refType = null;
        if (attributeType == SAttributeType.REFERENCE) {
            refType = sObjectTypeDao.findById(refObjTypeId);
        }
        return sAttributeFactory.create(attrName, attributeType, refType);
    }

    @RequestMapping(value = "/addAttribute", method = RequestMethod.POST)
    public String addAttribute(
            @RequestParam(value = "attr") Long attrId,
            @RequestParam(value = "objtype") Long objectTypeId) {
        SAttribute attr = sAttributeDao.findById(attrId);
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        objType.addAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/addStaticAttribute", method = RequestMethod.POST)
    public String addStaticAttribute(
            @RequestParam(value = "attr") Long attrId,
            @RequestParam(value = "objtype") Long objectTypeId) {
        SAttribute attr = sAttributeDao.findById(attrId);
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        objType.addStaticAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/deleteObjType", method = RequestMethod.GET)
    public String deleteObjType(@RequestParam(value = "objtype") Long objectTypeId) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        sObjectTypeDao.delete(objType);
        return "redirect:/admin/attrManager";
    }

    @RequestMapping(value = "/unbindAttr", method = RequestMethod.GET)
    public String unbindAttr(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.unbindAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);

        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/unbindStaticAttr", method = RequestMethod.GET)
    public String unbindStaticAttr(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.unbindStaticAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);

        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/addStaticAttrValue", method = RequestMethod.POST)
    public String addStaticAttrValue(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId,
            @RequestParam(value = "text-value") String textValue) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        SAttrValue sAttrValue = sAttrValueFactory.create(attr, null, textValue);
        try {
            objType.addStaticAttrValue(sAttrValue);
            sObjectTypeDao.createOrUpdate(objType);
        } catch (InconsistentEntityException ex) {
        }


        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/deleteStaticAttrValue", method = RequestMethod.GET)
    public String deleteStaticAttrValue(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId,
            @RequestParam(value = "order") Long order) {

        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.deleteStaticAttrValue(attr, order);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/moveUpStaticAttrValue", method = RequestMethod.GET)
    public String moveUpStaticAttrValue(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId,
            @RequestParam(value = "order") Long order) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.moveUpStaticAttrValue(attr, order);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/moveDownStaticAttrValue", method = RequestMethod.GET)
    public String moveDownStaticAttrValue(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId,
            @RequestParam(value = "order") Long order) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.moveDownStaticAttrValue(attr, order);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }
}
