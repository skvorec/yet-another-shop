package org.yetanothershop.persistence;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@ContextConfiguration(locations = {
    "classpath*:/META-INF/spring/*-test.xml",
    "classpath*:/META-INF/spring/context.xml",
    "classpath*:/META-INF/spring/hibernate-context.xml"
})
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AbstractSpringTest extends AbstractTestNGSpringContextTests
{
}