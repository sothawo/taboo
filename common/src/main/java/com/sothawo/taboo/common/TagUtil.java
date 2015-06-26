/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Utility class for Tags.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public final class TagUtil {
// -------------------------- STATIC METHODS --------------------------

    /**
     * splits a given String in separate Tags. Tags are consecutive occurences of alphanumerical characters. Every other
     * character is reagrded as separating tags.
     *
     * @param s
     *         the input String
     * @return the separated Tags.
     */
    public static Collection<String> split(final String s) {
        return (null == s || s.isEmpty())
                ? Collections.emptyList()
                : Arrays.asList(s.toLowerCase().split("[^a-zA-ZäöüÄÖÜß0-9]+")).stream().filter(t -> !t.isEmpty())
                .collect(Collectors.toSet());
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * private ctor.
     */
    private TagUtil() {
    }
}
