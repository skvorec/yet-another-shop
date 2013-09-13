package org.yetanothershop.web.tags;

import javax.servlet.jsp.JspWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SAttribute;
import static org.yetanothershop.persistence.entities.SAttributeType.*;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public class DisplayAttributeValues extends RequestContextAwareTag {

    @Autowired
    private SObjectTypeDao sObjectTypeDao;
    @Autowired
    private SAttributeDao sAttributeDao;
    //tag attributes
    private Long attributeId;
    private Long objectId;
    private Long objectTypeId;

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public void setObjectTypeId(Long objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        JspWriter out = pageContext.getOut();
        WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();
        AutowireCapableBeanFactory awFactory = webApplicationContext.getAutowireCapableBeanFactory();
        awFactory.autowireBean(this);
        SAttribute attribute = sAttributeDao.findById(attributeId);
        String contextPath = pageContext.getServletContext().getContextPath();


        CommonAVRenderer renderer = null;
        switch (attribute.getType()) {
            case TEXT:
                if (objectTypeId != null) {
                    SObjectType objectType = sObjectTypeDao.findById(objectTypeId);
                    renderer = new TextSAVRenderer(contextPath, attribute, objectType);
                }
                break;
            case PICTURE:
                if (objectTypeId != null) {
                    SObjectType objectType = sObjectTypeDao.findById(objectTypeId);
                    renderer = new PictureSAVRenderer(contextPath, attribute, objectType);
                }
                break;
            default:
            //do nothing
        }

        if (renderer != null) {
            out.println(renderer.getHtml());
        } else {
            out.println("such attr type is not processed now");
        }


        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
}
