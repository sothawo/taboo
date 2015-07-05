package com.sothawo.taboo.repositories;

import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.BookmarkRepositoryFactory;
import com.sothawo.taboo.common.NotFoundException;
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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
        Bookmark bookmarkIn = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").build();

        Bookmark bookmarkOut = repository.createBookmark(bookmarkIn);

        assertThat(bookmarkOut, is(notNullValue()));
        assertThat(bookmarkOut.getId(), is(notNullValue()));
        assertThat(bookmarkOut.getUrl(), is(bookmarkIn.getUrl()));
        assertThat(bookmarkOut.getTitle(), is(bookmarkIn.getTitle()));
        assertThat(bookmarkOut.getTags().size(), is(1));
        assertThat(bookmarkOut.getTags().iterator().next(), is(bookmarkIn.getTags().iterator().next()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBookmarkWithId() throws Exception {
        Bookmark bookmarkIn = aBookmark().withId("11").withUrl("url1").withTitle("title1").addTag("tag1").build();

        repository.createBookmark(bookmarkIn);
        fail("expected IllegalArgumentException");
    }

    @Test(expected = AlreadyExistsException.class)
    public void createBookmarksWithExistingUrl() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag2").build();

        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        fail("expected AlreadyExistsException");
    }

    @Test
    public void deleteExistingBookmark() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("title2").addTag("tag2").build();

        bookmark1 = repository.createBookmark(bookmark1);
        bookmark2 = repository.createBookmark(bookmark2);

        repository.deleteBookmark(bookmark2.getId());
        Collection<Bookmark> allBookmarks = repository.findAllBookmarks();

        assertThat(allBookmarks, hasSize(1));
        assertThat(allBookmarks, hasItems(bookmark1));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExistingBookmark() throws Exception {
        repository.deleteBookmark("42");

        fail("expected NotFoundException");
    }

    @Test
    public void findAllBookmarks() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("title2").addTag("tag2").build();

        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);

        Collection<Bookmark> allBookmarks = repository.findAllBookmarks();
        assertThat(allBookmarks, hasItems(bookmark1, bookmark2));
    }

    @Test
    public void findAllTags() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").addTag("common").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("title2").addTag("tag2").addTag("common").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("title3").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<String> tags = repository.findAllTags();

        assertThat(tags, hasSize(4));
        assertThat(tags, hasItems("tag1", "tag2", "tag3", "common"));
    }

    @Test
    public void findBookmarkById() throws Exception {
        Bookmark bookmark =
                repository.createBookmark(aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").build());

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
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").addTag("common").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("title2").addTag("tag2").addTag("common").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("title3").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.findBookmarksWithTags(Arrays.asList("tag2", "common"), true);

        assertThat(bookmarks, hasSize(1));
        assertThat(bookmarks, hasItem(bookmark2));
    }

    @Test
    public void findBookmarksWithTagsOr() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("title1").addTag("tag1").addTag("common").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("title2").addTag("tag2").addTag("common").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("title3").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.findBookmarksWithTags(Arrays.asList("tag2", "common"), false);

        assertThat(bookmarks, hasSize(2));
        assertThat(bookmarks, hasItems(bookmark1, bookmark2));
    }

    @Test
    public void findTagsOnNewRepositoryYieldsEmptyList() throws Exception {
        Collection<String> tags = repository.findAllTags();

        assertThat(tags, hasSize(0));
    }

    @Test
    public void findTagsWhenNoTagsExistYieldsEmptyList() throws Exception {
        Bookmark bookmark = aBookmark().withUrl("url1").withTitle("title1").build();
        repository.createBookmark(bookmark);

        Collection<String> tags = repository.findAllTags();

        assertThat(tags, hasSize(0));
    }

    @Test
    public void repositoryHasNoBookmarksOnCreation() throws Exception {
        Collection<Bookmark> bookmarks = repository.findAllBookmarks();
        assertThat(bookmarks, hasSize(0));
    }

    @Before
    public void setupRepository() throws Exception {
        repository = repositoryFactoryClass.newInstance().createRepository(factoryOptions);
        repository.purge();
    }

    @Test
    public void findBookmarkBySearchInTitle() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("Hello world").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("world wide web").build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("say hello").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.findBookmarksWithSearch("hello");

        assertThat(bookmarks, hasSize(2));
        assertThat(bookmarks, hasItems(bookmark1, bookmark3));
    }

    @Test
    public void findBookmarksBySearchInTitleAndTags() throws Exception {
        Bookmark bookmark1 = aBookmark().withUrl("url1").withTitle("Hello world").addTag("tag1").build();
        Bookmark bookmark2 = aBookmark().withUrl("url2").withTitle("world wide web").addTag("tag1").addTag("tag2")
                .build();
        Bookmark bookmark3 = aBookmark().withUrl("url3").withTitle("say hello").addTag("tag3").build();
        repository.createBookmark(bookmark1);
        repository.createBookmark(bookmark2);
        repository.createBookmark(bookmark3);

        Collection<Bookmark> bookmarks =
                repository.findBookmarksWithTagsAndSearch(Arrays.asList("tag1"), true, "hello");

        assertThat(bookmarks, hasSize(1));
        assertThat(bookmarks, hasItems(bookmark1));
    }
}
