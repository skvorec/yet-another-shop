package org.yetanothershop.web.tags;

import javax.servlet.jsp.JspWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SAttribute;
import static org.yetanothershop.persistence.entities.SAttributeType.TEXT;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public class DisplayAttributeValues extends RequestContextAwareTag
{
    private static final String TEMPLATES_LOCATION = "/tag-velocity-templates/";
    private static final String ADD_STATIC_ATTR_VALUE = "/admin/attrManager/addStaticAttrValue";
    private static final String DELETE_STATIC_ATTR_VALUE = "/admin/attrManager/deleteStaticAttrValue";
    private static final String MOVE_UP_STATIC_ATTR_VALUE = "/admin/attrManager/moveUpStaticAttrValue";
    private static final String MOVE_DOWN_STATIC_ATTR_VALUE = "/admin/attrManager/moveDownStaticAttrValue";
    @Autowired
    private SObjectTypeDao sObjectTypeDao;
    @Autowired
    private SAttributeDao sAttributeDao;
    //tag attributes
    private Long attributeId;
    private Long objectId;
    private Long objectTypeId;


    public void setAttributeId(Long attributeId)
    {
        this.attributeId = attributeId;
    }


    public void setObjectId(Long objectId)
    {
        this.objectId = objectId;
    }


    public void setObjectTypeId(Long objectTypeId)
    {
        this.objectTypeId = objectTypeId;
    }


    @Override
    protected int doStartTagInternal() throws Exception
    {
        JspWriter out = pageContext.getOut();
        WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();
        AutowireCapableBeanFactory awFactory = webApplicationContext.getAutowireCapableBeanFactory();
        awFactory.autowireBean(this);
        SAttribute attribute = sAttributeDao.findById(attributeId);
        String contextPath = pageContext.getServletContext().getContextPath();

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template templ = null;
        switch (attribute.getType()) {
            case TEXT:
                templ = ve.getTemplate(TEMPLATES_LOCATION + "sav-text.vm", "UTF-8");
                break;
            default:
            //do nothing
        }

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("contextPath", contextPath);
        velocityContext.put("attributeId", attributeId);

        if (objectTypeId != null) {
            velocityContext.put("objectTypeId", objectTypeId);
            velocityContext.put("deleteStaticAttrValue", DELETE_STATIC_ATTR_VALUE);
            velocityContext.put("moveUpStaticAttrValue", MOVE_UP_STATIC_ATTR_VALUE);
            velocityContext.put("moveDownStaticAttrValue", MOVE_DOWN_STATIC_ATTR_VALUE);
            velocityContext.put("addStaticAttrValue", ADD_STATIC_ATTR_VALUE);
            SObjectType objectType = sObjectTypeDao.findById(objectTypeId);
            velocityContext.put("allAV", objectType.getStaticAttrValues(attribute));
        }

        if (templ != null) {
            templ.merge(velocityContext, out);
        }
        else {
            out.println("such attr type is not processed now");
        }


        return SKIP_BODY;
    }


    @Override
    public int doEndTag()
    {
        return EVAL_PAGE;
    }
}
