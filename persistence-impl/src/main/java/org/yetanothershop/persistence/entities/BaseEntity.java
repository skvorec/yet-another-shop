package org.yetanothershop.persistence.entities;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 */
@MappedSuperclass
public class BaseEntity implements Serializable, Identifiable
{
    @Id
    @GeneratedValue
    protected Long id;


    @Override
    public Long getId()
    {
        return id;
    }
}