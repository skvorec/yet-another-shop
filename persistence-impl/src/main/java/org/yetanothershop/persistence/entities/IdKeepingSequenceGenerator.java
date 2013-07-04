package org.yetanothershop.persistence.entities;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IncrementGenerator;

/**
 *
 */
public class IdKeepingSequenceGenerator extends IncrementGenerator
{
    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException
    {
        if (object instanceof BaseEntityImpl) {
            BaseEntityImpl persistent = (BaseEntityImpl) object;
            if (persistent.getId() != null && persistent.getId() > 0) {
                return persistent.getId();
            }
        }
        return super.generate(session, object);
    }
}