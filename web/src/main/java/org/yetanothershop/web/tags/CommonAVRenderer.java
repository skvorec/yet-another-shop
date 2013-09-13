package org.yetanothershop.web.tags;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.yetanothershop.persistence.entities.SAttribute;

public abstract class CommonAVRenderer {

    public static final String TEMPLATES_LOCATION = "/tag-velocity-templates/";
    protected VelocityContext velocityContext = new VelocityContext();
    protected VelocityEngine ve = new VelocityEngine();
    protected Template templ;
    protected SAttribute attribute;   

    public CommonAVRenderer(String contextPath, SAttribute attribute) throws Exception {
        this.attribute = attribute;
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        velocityContext.put("contextPath", contextPath);
        velocityContext.put("attributeId", attribute.getId());
    }

    public abstract String getHtml() throws Exception;
}
