/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.repositories;

import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.BookmarkRepositoryFactory;

/**
 * Factory for InMemoryRepository instances.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class InMemoryRepositoryFactory implements BookmarkRepositoryFactory {
    /**
     * create a BookmarkRepository.
     *
     * @param options
     *         options for creating the repository
     * @return the created Repository
     */
    @Override
    public BookmarkRepository createRepository(String[] options) {
        return new InMemoryRepository();
    }
}
