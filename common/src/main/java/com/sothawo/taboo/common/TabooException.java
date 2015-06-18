/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

/**
 * base class for exceptions of the taboo service. Extends RuntimeException.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class TabooException extends RuntimeException {
// --------------------------- CONSTRUCTORS ---------------------------

    public TabooException(String message) {
        super(message);
    }
}
