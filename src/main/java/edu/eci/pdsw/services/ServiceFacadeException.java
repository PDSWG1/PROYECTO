/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.services;

/**
 *
 * @author Cesar
 */
public class ServiceFacadeException extends Exception {

    public ServiceFacadeException(String message) {
        super(message);
    }

    public ServiceFacadeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceFacadeException(Throwable cause) {
        super(cause);
    }
}
