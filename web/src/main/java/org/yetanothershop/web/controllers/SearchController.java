package org.yetanothershop.web.controllers;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
@Controller
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping(value = "/admin/search")
public class SearchController
{
    private SObjectTypeDao sObjectTypeDao;
    private SAttributeDao sAttributeDao;


    public void setsObjectTypeDao(SObjectTypeDao sObjectTypeDao)
    {
        this.sObjectTypeDao = sObjectTypeDao;
    }


    public void setsAttributeDao(SAttributeDao sAttributeDao)
    {
        this.sAttributeDao = sAttributeDao;
    }


    @ResponseBody
    @RequestMapping(value = "/objtype", method = RequestMethod.POST)
    public ResponseEntity<String> searchObjType(@RequestParam(value = "name", defaultValue = "none") String objTypeName)
            throws JSONException
    {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        responseHeaders.setCacheControl("no-cache, max-age=0");

        List<SObjectType> foundObjTypes = sObjectTypeDao.findByPartOfName(objTypeName);

        JSONObject jsonObj = new JSONObject();
        for (SObjectType foundObjType : foundObjTypes) {
            jsonObj.put(foundObjType.getId().toString(), foundObjType.getName());
        }
        return new ResponseEntity<String>(jsonObj.toString(1), responseHeaders, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/attribute", method = RequestMethod.POST)
    public ResponseEntity<String> searchAttribute(@RequestParam(value = "name", defaultValue = "none") String attrName)
            throws JSONException
    {
        System.out.println("Attr name: " + attrName);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        responseHeaders.setCacheControl("no-cache, max-age=0");

        List<SAttribute> foundAttrs = sAttributeDao.findByPartOfName(attrName);

        JSONObject jsonObj = new JSONObject();
        for (SAttribute foundAttr : foundAttrs) {
            jsonObj.put(foundAttr.getId().toString(), foundAttr.getName());
        }
        return new ResponseEntity<String>(jsonObj.toString(1), responseHeaders, HttpStatus.OK);
    }
}
