/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

/**
 * classes implementing this interface are used to create BookmarkRepository instances.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public interface BookmarkRepositoryFactory {
// -------------------------- OTHER METHODS --------------------------

    /**
     * create a BookmarkRepository
     *
     * @param options
     *         options for creating the repository
     * @return the created Repository
     */
    BookmarkRepository createRepository(String[] options);
}
