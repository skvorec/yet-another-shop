package org.yetanothershop.web.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
import org.yetanothershop.web.YashConstants;

/**
 *
 */
@Controller
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping(value = "/admin/attrManager")
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
    @RequestMapping(value = "/newObjectType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String createNewObjectType(@RequestParam(value = "name") String name) throws JSONException {

        name = name.trim();
        SObjectType newObjType = sObjectTypeFactory.create(name);
        sObjectTypeDao.createOrUpdate(newObjType);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(newObjType.getId().toString(), name);
        return jsonObj.toString(1);

    }

    @RequestMapping(value = "/deleteObjType", method = RequestMethod.GET)
    public String deleteObjType(@RequestParam(value = "objtype") Long objectTypeId) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        sObjectTypeDao.delete(objType);
        return "redirect:/admin/attrManager";
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

        attrName = attrName.trim();
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

        attrName = attrName.trim();
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

    @RequestMapping(value = "/unbindAttribute", method = RequestMethod.GET)
    public String unbindAttribute(
            @RequestParam(value = "objtype") Long objectTypeId,
            @RequestParam(value = "attr") Long attrId) {
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        objType.unbindAttribute(attr);
        sObjectTypeDao.createOrUpdate(objType);

        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }

    @RequestMapping(value = "/unbindStaticAttribute", method = RequestMethod.GET)
    public String unbindStaticAttribute(
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

    @RequestMapping(value = "/addStaticAttrPicture", method = RequestMethod.POST)
    public String addStaticAttrPicture(HttpServletRequest request) throws IOException, ServletException, FileUploadException {
        Long objectTypeId = null;
        Long attrId = null;

        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField()) {
                if (item.getFieldName().equals("objtype")) {
                    objectTypeId = Long.parseLong(item.getString());
                }
                if (item.getFieldName().equals("attr")) {
                    attrId = Long.parseLong(item.getString());
                }
            }
        }
        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attribute = sAttributeDao.findById(attrId);
        for (FileItem item : items) {
            if (!item.isFormField()) {
                String originalName = FilenameUtils.getName(item.getName());
                String extension = "";
                if (originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf(".") + 1);
                }
                InputStream inputStream = item.getInputStream();
                String newFileName = objType.getId() + "-" + attribute.getId() + "-" + System.currentTimeMillis() + "." + extension;
                String realPath = request.getServletContext().getRealPath(YashConstants.UPLOAD_DIR);
                FileUtils.writeByteArrayToFile(new File(realPath + "/" + newFileName), IOUtils.toByteArray(inputStream));

                SAttrValue sAttrValue = sAttrValueFactory.create(attribute, null, newFileName);
                try {
                    objType.addStaticAttrValue(sAttrValue);
                    sObjectTypeDao.createOrUpdate(objType);
                } catch (InconsistentEntityException ex) {
                }
            }
        }

        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }


    @RequestMapping(value = "/deleteStaticAttrPicture", method = RequestMethod.GET)
    public String deleteStaticAttrPicture(HttpServletRequest request) throws IOException {
        Long objectTypeId = Long.parseLong(request.getParameter("objtype"));
        Long attrId = Long.parseLong(request.getParameter("attr"));
        Integer order = Integer.parseInt(request.getParameter("order"));

        SObjectType objType = sObjectTypeDao.findById(objectTypeId);
        SAttribute attr = sAttributeDao.findById(attrId);
        //the first attr value has order = 1
        String fileName = objType.getStaticAttrValue(attr, order).getAttrValue();
        String realPath = request.getServletContext().getRealPath(YashConstants.UPLOAD_DIR);

        FileUtils.moveFile(new File(realPath, fileName), new File(realPath, "deleted-" + fileName));

        objType.deleteStaticAttrValue(attr, order);
        sObjectTypeDao.createOrUpdate(objType);
        return "redirect:/admin/attrManager?objtype=" + objectTypeId;
    }


}
