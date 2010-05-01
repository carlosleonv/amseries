/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badbuxtour.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badbuxtour.pojo.BBTUtil;

/**
 * 
 * @author eeugene
 */
public class GenericJpaController<T> {

    private Logger logger = LoggerFactory.getLogger(GenericJpaController.class);

    private Class<T> entityClass;

    public GenericJpaController() {
    }

    public GenericJpaController(T entity) {
	this();
	entityClass = (Class<T>) entity.getClass();
    }

    public GenericJpaController(Class<T> entityClass) {
	this();
	this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
	return EMF.get().createEntityManager();
    }

    public void create(T object) throws Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    em.persist(object);
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (em != null) {
		if (em.getTransaction().isActive())
		    em.getTransaction().rollback();
		em.close();
	    }
	}
    }

    public void edit(T object) throws Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    object = em.merge(object);
	    em.getTransaction().commit();
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (em != null) {
		if (em.getTransaction().isActive())
		    em.getTransaction().rollback();
		em.close();
	    }
	}
    }

    public void destroy(String id) throws Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    em.getTransaction().begin();
	    T object;
	    try {
		object = em.getReference(entityClass, id);
	    } catch (EntityNotFoundException enfe) {
		throw new Exception(enfe);
	    }
	    em.remove(object);
	    em.getTransaction().commit();
	} finally {
	    if (em != null) {
		if (em.getTransaction().isActive())
		    em.getTransaction().rollback();
		em.close();
	    }
	}
    }

    public List<T> findEntities() {
	return findEntities(true, -1, -1);
    }

    public List<T> findEntities(int maxResults, int firstResult) {
	return findEntities(false, maxResults, firstResult);
    }

    private List<T> findEntities(boolean all, int maxResults, int firstResult) {
	EntityManager em = getEntityManager();
	try {
	    Query q = em.createNamedQuery(BBTUtil.getClassName(entityClass) + ".findAll");
	    if (!all) {
		q.setMaxResults(maxResults);
		q.setFirstResult(firstResult);
	    }
	    List<T> list = q.getResultList();
	    list.size();
	    return list;
	} finally {
	    // em.close();
	}
    }

    public List<T> findBy(String queryName, Map<String, Object> params) throws Exception {
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    Query q = em.createNamedQuery(BBTUtil.getClassName(entityClass) + "." + queryName);
	    if (params != null)
		for (Entry<String, Object> i : params.entrySet()) {
		    q.setParameter(i.getKey(), i.getValue());
		}
	    List<T> list = q.getResultList();
	    list.size();
	    return list;
	} finally {
	    // em.close();
	}
    }

    public T findEntity(Object id) {
	if (logger.isDebugEnabled()) {
	    logger.debug("findEntity : " + id);
	}
	EntityManager em = null;
	try {
	    em = getEntityManager();
	    return em.find(entityClass, id);
	} finally {
	    // em.close();
	}
    }

    public int count() {
	EntityManager em = getEntityManager();
	try {
	    Query q = em.createQuery("select count(o) from " + BBTUtil.getClassName(entityClass) + " as o");
	    return ((Integer) q.getSingleResult()).intValue();
	} finally {
	    em.close();
	}
    }
}
