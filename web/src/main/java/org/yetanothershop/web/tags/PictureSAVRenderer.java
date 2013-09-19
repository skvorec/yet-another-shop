package org.yetanothershop.web.tags;

import java.io.StringWriter;
import org.yetanothershop.persistence.entities.SAttribute;
import org.yetanothershop.persistence.entities.SObjectType;
import org.yetanothershop.web.YashConstants;
import static org.yetanothershop.web.tags.CommonAVRenderer.TEMPLATES_LOCATION;

public class PictureSAVRenderer extends CommonAVRenderer {

    private static final String ADD_STATIC_ATTR_PICTURE = "/admin/attrManager/addStaticAttrPicture";
    private static final String DELETE_STATIC_ATTR_PICTURE = "/admin/attrManager/deleteStaticAttrPicture";
    private static final String MOVE_UP_STATIC_ATTR_VALUE = "/admin/attrManager/moveUpStaticAttrValue";
    private static final String MOVE_DOWN_STATIC_ATTR_VALUE = "/admin/attrManager/moveDownStaticAttrValue";
    private final SObjectType objectType;
    

    public PictureSAVRenderer(String contextPath, SAttribute attribute, SObjectType objectType)
            throws Exception {
        super(contextPath, attribute);
      
        this.objectType = objectType;
        templ = ve.getTemplate(TEMPLATES_LOCATION + "sav-picture.vm", "UTF-8");
        velocityContext.put("objectTypeId", objectType.getId());
    }

    @Override
    public String getHtml() throws Exception {
        velocityContext.put("addStaticAttrPicture", ADD_STATIC_ATTR_PICTURE);
        velocityContext.put("deleteStaticAttrPicture", DELETE_STATIC_ATTR_PICTURE);
        velocityContext.put("moveUpStaticAttrValue", MOVE_UP_STATIC_ATTR_VALUE);
        velocityContext.put("moveDownStaticAttrValue", MOVE_DOWN_STATIC_ATTR_VALUE);
        velocityContext.put("uploadDir", YashConstants.UPLOAD_DIR);
        velocityContext.put("allAV", objectType.getStaticAttrValues(attribute));
        StringWriter result = new StringWriter();
        templ.merge(velocityContext, result);
        return result.toString();
    }
}
