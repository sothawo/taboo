package com.sothawo.taboo.repositories;

import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@RunWith(Parameterized.class)
public class RepositoryTest {
// ------------------------------ FIELDS ------------------------------

    @Parameter(0)
    public Class<BookmarkRepository> repositoryClass;

// -------------------------- STATIC METHODS --------------------------

    @Parameters
    public static List<Object[]> parameters() {
        return Arrays.asList(new Object[][]{{InMemoryRepository.class}});
    }

// -------------------------- OTHER METHODS --------------------------

    @Test
    public void createBookmark() throws Exception {
        BookmarkRepository repository = repositoryClass.newInstance();
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
        BookmarkRepository repository = repositoryClass.newInstance();
        Bookmark bookmarkIn = aBookmark().withId(11).withUrl("url1").addTag("tag1").build();

        repository.createBookmark(bookmarkIn);
        fail("expected IllegalArgumentException");
    }

    @Test(expected = AlreadyExistsException.class)
    public void createBookmarksWithExistingUrl() throws Exception {
        BookmarkRepository repository = repositoryClass.newInstance();
        Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url1").addTag("tag2").build();

        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        fail("expected AlreadyExistsException");
    }

    @Test
    public void findAllBookmarks() throws Exception {
        BookmarkRepository repository = repositoryClass.newInstance();
        Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").addTag("tag2").build();

        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);

        Collection<Bookmark> allBookmarks = repository.findAllBookmarks();
        assertThat(allBookmarks, hasItems(bookmark1, bookmark2));
    }

    @Test
    public void findBookmarkById() throws Exception {
        BookmarkRepository repository = repositoryClass.newInstance();
        Bookmark bookmark = repository.createBookmark(aBookmark().withUrl("url1").addTag("tag1").build());

        Bookmark foundBookmark = repository.findBookmarkById(bookmark.getId());

        assertThat(foundBookmark, is(bookmark));
    }

    @Test(expected = NotFoundException.class)
    public void findBookmarkByNotExistingId() throws Exception {
        BookmarkRepository repository = repositoryClass.newInstance();
        repository.findBookmarkById(42);
        fail("NotFoundException expected");
    }
}
