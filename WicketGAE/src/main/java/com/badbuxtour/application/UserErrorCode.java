/*
 * UserErrorCode.java
 * 
 * Created on Jul 23, 2007, 10:51:18 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.badbuxtour.application;

/**
 * Error codes that can be returned by the login and add user methods on
 * UserDatabase
 *
 * @author Tim Boudreau
 */
    
public enum UserErrorCode {
    PASSWORD_EMPTY_OR_NULL, CLUB_EXISTS, USER_EXISTS, BAD_PASSWORD, INCORRECT_PASSWORD,
    UNKNOWN_USER, NON_MATCHING_PASSWORD, EXCEPTION, EMPTY_EMAIL, BAD_EMAIL, INACTIVE;
    //XXX localize
    @Override
    public String toString() {
        switch (this) {
        case BAD_PASSWORD :
            return "Mot de passe incorrect";
        case CLUB_EXISTS :
            return "Un club avec ce sigle existe déjà  !";
        case USER_EXISTS :
            return "Un utilisateur avec ce mail existe déjà  !";
        case PASSWORD_EMPTY_OR_NULL :
            return "Mot de passe requis";
        case INCORRECT_PASSWORD :
            return "Mot de passe incorrect";
        case UNKNOWN_USER :
            return "Utilisateur inconnu";
        case NON_MATCHING_PASSWORD :
            return "Mots de passe différents";
        case EXCEPTION :
            return "Une erreur s'est produite";
        case EMPTY_EMAIL :
            return "Adresse mail requise";
        case BAD_EMAIL :
            return "Adresse mail invalide";
        case INACTIVE :
            return "Compte inactif";
        default :
            throw new AssertionError();
        }
    }
}