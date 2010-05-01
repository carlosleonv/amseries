/*
 * HomePage.java
 *
 * Created on 17 janvier 2010, 14:01
 */
 
package com.badbuxtour.application;

@RequiresSignin
public class HomePage extends BasePage {

    public HomePage() {
        BusinessPanel bp = new BusinessPanel("businessPanel");
        add(bp);
    }
}
