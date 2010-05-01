package com.badbuxtour.controller;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author eeugene
 * 
 */
public final class EMF {
    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("badbuxtour");

    private EMF() {
    }

    public static EntityManagerFactory get() {
	return emfInstance;
    }
}