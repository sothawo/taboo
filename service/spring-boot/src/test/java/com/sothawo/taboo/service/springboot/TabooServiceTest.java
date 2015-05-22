package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.NotFoundException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class TabooServiceTest {
// -------------------------- OTHER METHODS --------------------------

    @Test
    public void getAllBookmarks(@Mocked final BookmarkRepository repository) throws Exception {
        List<Bookmark> bookmarks = createBookmarks(23, 42);

        new Expectations() {{
            repository.findAllBookmarks();
            result = bookmarks;
        }};

        MockMvc mockMvc = standaloneSetup(new TabooService(repository)).build();
        mockMvc.perform(get("/taboo/bookmark").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(bookmarks.get(0).getId())))
                .andExpect(jsonPath("$[0].url", is(bookmarks.get(0).getUrl())))
                .andExpect(jsonPath("$[0].tags[0]", is(bookmarks.get(0).getTags().iterator().next())))
                .andExpect(jsonPath("$[1].id", is(bookmarks.get(1).getId())))
                .andExpect(jsonPath("$[1].url", is(bookmarks.get(1).getUrl())))
                .andExpect(jsonPath("$[1].tags[0]", is(bookmarks.get(1).getTags().iterator().next())))
        ;

        new Verifications() {{
            repository.findAllBookmarks();
            times = 1;
        }};
    }

    @Test
    public void findExistingBookmark(@Mocked final BookmarkRepository repository) throws Exception {
        final Bookmark bookmark = createBookmarks(7, 11).get(1);
        new Expectations() {{
            repository.findBookmarkById(11);
            result = bookmark;
        }};

        MockMvc mockMvc = standaloneSetup(new TabooService(repository)).build();
        mockMvc.perform(get("/taboo/bookmark/{id}", bookmark.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookmark.getId())))
                .andExpect(jsonPath("$.url", is(bookmark.getUrl())))
                .andExpect(jsonPath("$.tags[0]", is(bookmark.getTags().iterator().next())))
        ;

        new Verifications() {{
            repository.findBookmarkById(11);
            times = 1;
        }};

    }

    @Test
    public void findNotExistingBookmarkYieldsNotFound(@Mocked final BookmarkRepository repository) throws Exception {
        List<Bookmark> bookmarks = createBookmarks(23, 42);
        new Expectations() {{
            repository.findBookmarkById(11);
            result = new NotFoundException();
        }};

        MockMvc mockMvc = standaloneSetup(new TabooService(repository)).build();
        mockMvc.perform(get("/taboo/bookmark/{id}", 11).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;

        new Verifications() {{
            repository.findBookmarkById(11);
            times = 1;
        }};
    }

    /**
     * helper method to create a list of bookmarks.
     *
     * @param ids
     *         id values for the Bookmark objects to create
     * @return a list of bookmarks
     */
    private List<Bookmark> createBookmarks(int... ids) {
        List<Bookmark> bookmarks = new ArrayList<>();
        for (int id : ids) {
            bookmarks.add(aBookmark().withId(id).withUrl("url" + id).addTag("tag" + id).build());
        }
        return bookmarks;
    }
}
