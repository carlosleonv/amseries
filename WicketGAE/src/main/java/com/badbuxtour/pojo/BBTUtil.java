/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badbuxtour.pojo;

/**
 *
 * @author eeugene
 */
public class BBTUtil {

    public static String getClassName(Class c) {
        String FQClassName = c.getName();
        int firstChar;
        firstChar = FQClassName.lastIndexOf('.') + 1;
        if (firstChar > 0) {
            FQClassName = FQClassName.substring(firstChar);
        }
        return FQClassName;
    }
}
