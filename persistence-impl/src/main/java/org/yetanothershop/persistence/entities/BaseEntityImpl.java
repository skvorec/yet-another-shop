package org.yetanothershop.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 */
@MappedSuperclass
public class BaseEntityImpl implements Serializable, BaseEntity
{
    @Id
    @GeneratedValue
    protected Long id;
    @Column(unique = true)
    protected String name;


    @Override
    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    @Override
    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }
}