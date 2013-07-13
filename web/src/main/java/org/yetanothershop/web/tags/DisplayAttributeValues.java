package org.yetanothershop.web.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.yetanothershop.persistence.daos.SAttributeDao;
import org.yetanothershop.persistence.daos.SObjectTypeDao;
import org.yetanothershop.persistence.entities.SAttrValue;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObjectType;

/**
 *
 */
public class DisplayAttributeValues extends RequestContextAwareTag {

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
    protected int doStartTagInternal() throws IOException {
        JspWriter out = pageContext.getOut();
        WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();
        AutowireCapableBeanFactory awFactory = webApplicationContext.getAutowireCapableBeanFactory();
        awFactory.autowireBean(this);
        SAttribute attribute = sAttributeDao.findById(attributeId);

        String contextPath = pageContext.getServletContext().getContextPath();
        if (objectTypeId != null) {
            SObjectType objectType = sObjectTypeDao.findById(objectTypeId);
            List<SAttrValue> staticAttrValues = objectType.getStaticAttrValues(attribute);
            //display
            out.println("<table class=\"attr-values-table\">");
            for (SAttrValue attrValue : staticAttrValues) {
                out.println("<tr>");
                out.println("<td>");
                //delete
                out.println("<a class=\"need-confirm\" href=\"" + contextPath + DELETE_STATIC_ATTR_VALUE
                        + "?objtype=" + objectTypeId + "&attr=" + attributeId + "&order=" + attrValue.getOrderNumber() + "\">\n"
                        + "  <img class=\"icon-img\" src=\"" + contextPath + "/img/delete.png\"/>\n"
                        + "</a>\n");
                out.println("<a class=\"need-confirm\" href=\"" + contextPath + MOVE_UP_STATIC_ATTR_VALUE
                        + "?objtype=" + objectTypeId + "&attr=" + attributeId + "&order=" + attrValue.getOrderNumber() + "\">\n"
                        + "  <img class=\"icon-img\" src=\"" + contextPath + "/img/up.png\"/>\n"
                        + "</a>\n");
                out.println("<a class=\"need-confirm\" href=\"" + contextPath + MOVE_DOWN_STATIC_ATTR_VALUE
                        + "?objtype=" + objectTypeId + "&attr=" + attributeId + "&order=" + attrValue.getOrderNumber() + "\">\n"
                        + "  <img class=\"icon-img\" src=\"" + contextPath + "/img/down.png\"/>\n"
                        + "</a>\n");

                switch (attribute.getType()) {
                    case TEXT:
                        out.println(attrValue.getAttrValue());
                        break;
                    default:
                    //do nothing
                }
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            //add new value
            out.println("Добавить новое значение");
            out.println("<form method=\"POST\" accept-charset=\"UTF-8\" "
                    + "action=\"" + contextPath + ADD_STATIC_ATTR_VALUE + "\">");
            out.println("<input type=\"hidden\" name=\"objtype\" value=\"" + objectTypeId + "\"/>");
            out.println("<input type=\"hidden\" name=\"attr\" value=\"" + attributeId + "\"/>");
            switch (attribute.getType()) {
                case TEXT:
                    out.println("<input type=\"text\" name=\"text-value\"/>");
                    break;
                default:
                //do nothing
            }
            out.println("<input type=\"submit\"/>");
            out.println("</form>");
        }


        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
}
