package org.yetanothershop.persistence.daos;

import javax.annotation.Resource;
import org.yetanothershop.persistence.AbstractSpringTest;

/**
 *
 */
public class SAttributeDaoTest extends AbstractSpringTest
{
    @Resource(name = "sAttributeDao")
    private SAttributeDao attributeDao;
    @Resource(name = "sObjectTypeDao")
    private SObjectTypeDao objTypeDao;
}
