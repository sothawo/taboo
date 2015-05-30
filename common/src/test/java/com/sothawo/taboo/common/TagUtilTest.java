/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.common;

import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for the TagUtil class.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class TagUtilTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void splitConvertsToLowerCase() throws Exception {
        Collection<String> tags = TagUtil.split("#+Eins zwei /DREI, vieR-");

        assertThat(tags.size(), is(4));
        assertThat((tags), hasItems("eins", "zwei", "drei", "vier"));
    }

    @Test
    public void splitEmptyYieldsEmptyCollection() throws Exception {
        assertThat(TagUtil.split("").isEmpty(), is(true));
    }

    @Test
    public void splitNullYieldsEmptyCollection() throws Exception {
        assertThat(TagUtil.split(null).isEmpty(), is(true));
    }

    @Test
    public void splitRemovesDuplicateTags() throws Exception {
        Collection<String> tags = TagUtil.split("#+eins zwei /EINS, zwei-");

        assertThat(tags.size(), is(2));
        assertThat((tags), hasItems("eins", "zwei"));
    }

    @Test
    public void splitSingleTag() throws Exception {
        String tag = "hallo";

        Collection<String> tags = TagUtil.split(tag);

        assertThat(tags.size(), is(1));
        assertThat((tags), hasItem(tag));
    }

    @Test
    public void splitTags() throws Exception {
        Collection<String> tags = TagUtil.split("#+eins zwei /drei, vier-");

        assertThat(tags.size(), is(4));
        assertThat((tags), hasItems("eins", "zwei", "drei", "vier"));
    }

    @Test
    public void splitWithNoValidCharactersYieldsEmptyCollection() throws Exception {
        assertThat(TagUtil.split("+*´ß ?=)( &&$").isEmpty(), is(true));
    }
}
