package org.yetanothershop.persistence.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SAttrValueHelper {

    public static List<SAttrValue> filterAndSort(List<SAttrValue> allAttrValues, SAttribute attribute) {
        List<SAttrValue> result = new ArrayList<SAttrValue>();
        for (SAttrValue sAttrValue : allAttrValues) {
            if (sAttrValue.getAttribute().equals(attribute)) {
                result.add(sAttrValue);
            }
        }
        Collections.sort(result, new Comparator<SAttrValue>() {
            @Override
            public int compare(SAttrValue o1, SAttrValue o2) {
                final SAttribute attribute1 = o1.getAttribute();
                final SAttribute attribute2 = o2.getAttribute();
                if (!attribute1.equals(attribute2)) {
                    return Long.compare(attribute1.getId(), attribute2.getId());
                }
                return Long.compare(o1.getOrderNumber(), o2.getOrderNumber());
            }
        });
        return result;
    }
}
