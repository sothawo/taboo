/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

/**
 * Exception thrown if something already exists.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class AlreadyExistsException extends TabooException {
// --------------------------- CONSTRUCTORS ---------------------------

    public AlreadyExistsException(String message) {
        super(message);
    }
}
