package com.sothawo.taboo.repositories;

import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import org.junit.Test;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class InMemoryRepositoryTest {

    @Test
    public void createBookmark() throws Exception {
        BookmarkRepository repository = new InMemoryRepository();
        Bookmark bookmarkIn = aBookmark().withUrl("url1").addTag("tag1").build();

        Bookmark bookmarkOut = repository.createBookmark(bookmarkIn);

        assertThat(bookmarkOut, is(notNullValue()));
        assertThat(bookmarkOut.getId(), is(notNullValue()));
        assertThat(bookmarkOut.getUrl(), is(bookmarkIn.getUrl()));
        assertThat(bookmarkOut.getTags().size(), is(1));
        assertThat(bookmarkOut.getTags().iterator().next(), is(bookmarkIn.getTags().iterator().next()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBookmarkWithId() throws Exception {
        BookmarkRepository repository = new InMemoryRepository();
        Bookmark bookmarkIn = aBookmark().withId(11).withUrl("url1").addTag("tag1").build();

        repository.createBookmark(bookmarkIn);
        fail("expected IllegalArgumentException");
    }

    @Test(expected = AlreadyExistsException.class)
    public void createBookmarksWithExistingUrl() throws Exception {
        BookmarkRepository repository = new InMemoryRepository();
        Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url1").addTag("tag2").build();

        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        fail("expected AlreadyExistsException");
    }
}
