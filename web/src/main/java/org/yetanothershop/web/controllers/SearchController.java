package org.yetanothershop.web.controllers;

import java.util.ArrayList;
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
import org.yetanothershop.persistence.daos.SObjectTypeDao;
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


    public void setsObjectTypeDao(SObjectTypeDao sObjectTypeDao)
    {
        this.sObjectTypeDao = sObjectTypeDao;
    }


    @ResponseBody
    @RequestMapping(value = "/objtype", method = RequestMethod.POST)
    public ResponseEntity<String> searchObjType(@RequestParam(value = "objtype", defaultValue = "none") String objTypeName)
            throws JSONException
    {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        responseHeaders.setCacheControl("no-cache, max-age=0");

        List<SObjectType> foundObjTypes = sObjectTypeDao.findByName(objTypeName);
        String names = "";
        for (SObjectType foundObjType : foundObjTypes) {
            names += foundObjType.getName() + ",";
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("inputParam", objTypeName);
        jsonObj.put("searchResult", names);
        return new ResponseEntity<String>(jsonObj.toString(1), responseHeaders, HttpStatus.OK);
    }
}
