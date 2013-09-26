package org.yetanothershop.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SAttrValueHelperTest {

    @Test
    public void getStaticAttrValues() {
        SAttributeImpl attr1 = new SAttributeImpl("Attr1", SAttributeType.TEXT, null);
        SAttributeImpl attr2 = new SAttributeImpl("Attr2", SAttributeType.TEXT, null);

        List<SAttrValue> attrValues = new ArrayList<SAttrValue>();

        attrValues.add(new SAttrValueImpl(attr1, "text1"));
        attrValues.add(new SAttrValueImpl(attr1, "text2"));
        attrValues.add(new SAttrValueImpl(attr2, "text3"));

        List<SAttrValue> attr1Values = SAttrValueHelper.filterAndSort(attrValues, attr1);
        Assert.assertEquals(attr1Values.size(), 2);
        Assert.assertEquals(attr1Values.get(0).getAttrValue(), "text1");
        Assert.assertEquals(attr1Values.get(1).getAttrValue(), "text2");
    }
}
