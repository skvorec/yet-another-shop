package org.yetanothershop.persistence.entities;

/**
 *
 */
public interface SAttribute extends Identifiable
{
    String getName();


    SAttributeType getType();
}