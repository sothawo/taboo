package com.sothawo.taboo.common;

import org.junit.Test;

import java.util.Collection;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class BookmarkTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void builderSetsAllFields() throws Exception {
        Bookmark bookmark = aBookmark()
                .withId("id").withUrl("url").withTitle("title").addTag("tag1").addTag("tag2").build();
        Collection<String> tags = bookmark.getTags();

        assertThat(bookmark.getId(), is("id"));
        assertThat(bookmark.getTitle(), is("title"));
        assertThat(bookmark.getUrl(), is("url"));
        assertThat(tags, hasSize(2));
        assertThat(tags, hasItems("tag1", "tag2"));
    }

    @Test
    public void twoBookmarksWithSameUrlAreEqual() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("uurrll").build();
        Bookmark bookmark2 = aBookmark().withUrl("uurrll").build();

        assertThat(bookmark1, not(sameInstance(bookmark2)));
        assertThat(bookmark1, equalTo(bookmark2));
    }

    @Test
    public void tagsAreLowercase() throws Exception {
        Collection<String> tags = aBookmark().addTag("ABC").build().getTags();

        assertThat(tags, hasSize(1));
        assertThat(tags, hasItem("abc"));
    }

    @Test
    public void tagsHaveNoDuplicates() throws Exception {
        Collection<String> tags = aBookmark().withUrl("url").addTag("ABC").addTag("abc").build().getTags();

        assertThat(tags, hasSize(1));
        assertThat(tags, hasItem("abc"));
    }
}
