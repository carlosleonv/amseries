/*
 * User.java
 * 
 * Created on Aug 15, 2007, 1:16:22 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.pojo;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * 
 * @author eeugene
 */
public class UserUtil {

    public static boolean matchPassword(String enterPassw, String password) {
	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	return passwordEncryptor.checkPassword(enterPassw, password);
    }

    public static String encryptPassword(String password) {
	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	String encryptedPassword = passwordEncryptor.encryptPassword(password);
	return encryptedPassword;
    }

}
