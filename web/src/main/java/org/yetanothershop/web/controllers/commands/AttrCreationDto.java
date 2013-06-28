package org.yetanothershop.web.controllers.commands;

/**
 *
 */
public class AttrCreationDto extends NameAwareDto
{
    private String attrType;
    private String objectTypeId;


    public String getAttrType()
    {
        return attrType;
    }


    public void setAttrType(String attrType)
    {
        this.attrType = attrType;
    }


    public String getObjectTypeId()
    {
        return objectTypeId;
    }


    public void setObjectTypeId(String objectTypeId)
    {
        this.objectTypeId = objectTypeId;
    }
}