/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badbuxtour.application;

import java.io.Serializable;

/**
 * 
 * @author eeugene
 */
public class SelectOption implements Serializable {

    private Object key;
    private String value;

    public SelectOption(Object key, String value) {
	this.key = key;
	this.value = value;
    }

    public Object getKey() {
	return key;
    }

    public void setKey(Object key) {
	this.key = key;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }
}
