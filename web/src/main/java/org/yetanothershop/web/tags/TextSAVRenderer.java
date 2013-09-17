package org.yetanothershop.web.tags;

import java.io.IOException;
import java.io.StringWriter;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObjectType;

public class TextSAVRenderer extends CommonAVRenderer {

    private static final String ADD_STATIC_ATTR_VALUE = "/admin/attrManager/addStaticAttrValue";
    private static final String DELETE_STATIC_ATTR_VALUE = "/admin/attrManager/deleteStaticAttrValue";
    private static final String MOVE_UP_STATIC_ATTR_VALUE = "/admin/attrManager/moveUpStaticAttrValue";
    private static final String MOVE_DOWN_STATIC_ATTR_VALUE = "/admin/attrManager/moveDownStaticAttrValue";
    private final SObjectType objectType;

    public TextSAVRenderer(String contextPath, SAttribute attribute, SObjectType objectType) throws Exception {
        super(contextPath, attribute);
        this.objectType = objectType;
        templ = ve.getTemplate(TEMPLATES_LOCATION + "sav-text.vm", "UTF-8");
        velocityContext.put("objectTypeId", objectType.getId());
    }

    @Override
    public String getHtml() throws IOException {
        velocityContext.put("deleteStaticAttrValue", DELETE_STATIC_ATTR_VALUE);
        velocityContext.put("moveUpStaticAttrValue", MOVE_UP_STATIC_ATTR_VALUE);
        velocityContext.put("moveDownStaticAttrValue", MOVE_DOWN_STATIC_ATTR_VALUE);
        velocityContext.put("addStaticAttrValue", ADD_STATIC_ATTR_VALUE);
        velocityContext.put("allAV", objectType.getStaticAttrValues(attribute));
        StringWriter result = new StringWriter();
        templ.merge(velocityContext, result);
        return result.toString();
    }
}
