package org.yetanothershop.persistence.entities;

/**
 *
 */
public class InconsistentEntityException extends Exception
{
    public InconsistentEntityException(String message)
    {
        super(message);
    }
}