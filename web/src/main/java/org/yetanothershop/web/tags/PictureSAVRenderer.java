package org.yetanothershop.web.tags;

import java.io.StringWriter;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObjectType;
import static org.yetanothershop.web.tags.CommonAVRenderer.TEMPLATES_LOCATION;

public class PictureSAVRenderer extends CommonAVRenderer {

    private static final String ADD_STATIC_ATTR_PICTURE = "/admin/attrManager/addStaticAttrPicture";
    protected SObjectType objectType;

    public PictureSAVRenderer(String contextPath, SAttribute attribute, SObjectType objectType) throws Exception {
        super(contextPath, attribute);
        this.objectType = objectType;
        templ = ve.getTemplate(TEMPLATES_LOCATION + "sav-picture.vm", "UTF-8");
        velocityContext.put("objectTypeId", objectType.getId());
    }

    @Override
    public String getHtml() throws Exception {
        velocityContext.put("addStaticAttrPicture", ADD_STATIC_ATTR_PICTURE);
        StringWriter result = new StringWriter();
        templ.merge(velocityContext, result);
        return result.toString();
    }
}
