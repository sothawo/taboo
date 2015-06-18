package com.sothawo.taboo.common;

import org.junit.Test;

import java.util.Collection;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class BookmarkTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void tagsAreLowercase() throws Exception {
        Bookmark bookmark = aBookmark().withUrl("url").addTag("ABC").build();

        assertThat(bookmark.getTags().iterator().next(), is("abc"));
    }

    @Test
    public void tagsHaveNoDuplicates() throws Exception {
        Bookmark bookmark = aBookmark().withUrl("url").addTag("ABC").addTag("abc").build();
        Collection<String> tags = bookmark.getTags();
        assertThat(tags.size(), is(1));
        assertThat(tags.iterator().next(), is("abc"));
    }
}
