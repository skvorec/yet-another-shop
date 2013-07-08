package org.yetanothershop.web.tags;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

/**
 *
 */
public class DisplayAttributeValues extends RequestContextAwareTag
{
    @Override
    protected int doStartTagInternal() throws Exception
    {
        WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();

        return SKIP_BODY;
    }


    @Override
    public int doEndTag()
    {
        return EVAL_PAGE;
    }
}