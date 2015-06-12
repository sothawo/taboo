package com.sothawo.taboo.repositories;

import com.sothawo.taboo.common.*;
import com.sothawo.taboo.repositories.springmongo.MongoConfig;
import com.sothawo.taboo.repositories.springmongo.SpringMongoRepositoryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@RunWith(Parameterized.class)
public class RepositoryTest {
// ------------------------------ FIELDS ------------------------------

    @Parameter(0)
    public Class<BookmarkRepositoryFactory> repositoryFactoryClass;

    @Parameter(1)
    public String[] factoryOptions;

    /** created in #setupRepository() method */
    private BookmarkRepository repository;

// -------------------------- STATIC METHODS --------------------------

    /**
     * get the parameters for the tests. For every test class execution this is a list of {BookmarkFactory.class,
     * String[] factoryOptions}
     *
     * @return test parameters
     */
    @Parameters
    public static List<Object[]> parameters() {
        return Arrays.asList(new Object[][]
                {
                        {InMemoryRepositoryFactory.class, null},
                        {SpringMongoRepositoryFactory.class, new String[]{MongoConfig.TEST_PROFILE}}
                        // Spring test profile
                });
    }

// -------------------------- OTHER METHODS --------------------------

    @Test
    public void createBookmark() throws Exception {
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
        Bookmark bookmarkIn = aBookmark().withId("11").withUrl("url1").addTag("tag1").build();

        repository.createBookmark(bookmarkIn);
        fail("expected IllegalArgumentException");
    }

    @Test(expected = AlreadyExistsException.class)
    public void createBookmarksWithExistingUrl() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url1").addTag("tag2").build();

        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        fail("expected AlreadyExistsException");
    }

    @Test
    public void findAllBookmarks() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").addTag("tag2").build();

        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);

        Collection<Bookmark> allBookmarks = repository.findAllBookmarks();
        assertThat(allBookmarks, hasItems(bookmark1, bookmark2));
    }

    @Test
    public void findBookmarkById() throws Exception {
        Bookmark bookmark = repository.createBookmark(aBookmark().withUrl("url1").addTag("tag1").build());

        Bookmark foundBookmark = repository.findBookmarkById(bookmark.getId());

        assertThat(foundBookmark, is(bookmark));
    }

    @Test(expected = NotFoundException.class)
    public void findBookmarkByIdNotExisting() throws Exception {
        repository.findBookmarkById("42");
        fail("NotFoundException expected");
    }

    @Test
    public void findBookmarksWithTagsAnd() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").addTag("common").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").addTag("tag2").addTag("common").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.findBookmarksWithTags(Arrays.asList("tag2", "common"), true);

        assertThat(bookmarks.size(), is(1));
        assertThat(bookmarks, hasItem(bookmark2));
    }

    @Test
    public void findBookmarksWithTagsOr() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").addTag("common").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").addTag("tag2").addTag("common").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.findBookmarksWithTags(Arrays.asList("tag2", "common"), false);

        assertThat(bookmarks.size(), is(2));
        assertThat(bookmarks, hasItems(bookmark1, bookmark2));
    }

    @Test
    public void repositoryHasNoBookmarksOnCreation() throws Exception {
        Collection<Bookmark> bookmarks = repository.findAllBookmarks();
        assertThat(bookmarks.size(), is(0));
    }

    @Before
    public void setupRepository() throws Exception {
        repository = repositoryFactoryClass.newInstance().createRepository(factoryOptions);
        repository.purge();
    }

}
