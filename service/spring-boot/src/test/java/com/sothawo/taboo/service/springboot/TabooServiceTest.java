package com.sothawo.taboo.service.springboot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.NotFoundException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class TabooServiceTest {
    private static final String TABOO_BOOKMARKS = "/taboo/bookmarks";

    // ------------------------------ FIELDS ------------------------------

    // the service to be tested, will be created by JMockit and injected with the repository which is mocked
    @Tested
    TabooService tabooService;
    @Injectable
    BookmarkRepository repository;

// -------------------------- OTHER METHODS --------------------------

    private byte[] convertObjectToJsonBytes(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(o);
    }

    @Test
    public void createBookmark() throws Exception {
        Bookmark bookmarkIn = aBookmark().withUrl("url").addTag("tag").build();
        Bookmark bookmarkOut = aBookmark().withId("11").withUrl("url").addTag("tag").build();

        new Expectations() {{
            repository.createBookmark(bookmarkIn);
            result = bookmarkOut;
        }};

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(post(TABOO_BOOKMARKS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(bookmarkIn)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith(TABOO_BOOKMARKS +
                        "/11")))
                .andExpect(jsonPath("$.id", is(bookmarkOut.getId())))
                .andExpect(jsonPath("$.url", is(bookmarkOut.getUrl())))
        ;
        new Verifications() {{
            repository.createBookmark(bookmarkIn);
            times = 1;
        }};
    }

    @Test
    public void createBookmarksWithExistingUrlYieldsConflict() throws Exception {
        Bookmark bookmarkIn = aBookmark().withUrl("url").addTag("tag").build();

        new Expectations() {{
            repository.createBookmark((Bookmark) any);
            result = new AlreadyExistsException("bookmark url");
        }};

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(post(TABOO_BOOKMARKS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(bookmarkIn)))
                .andExpect(status().isConflict())
        ;

        new Verifications() {{
            repository.createBookmark((Bookmark) any);
            times = 1;
        }};
    }

    @Test
    public void createBookmarksWithIdYieldsPreconditionFailed() throws Exception {
        Bookmark bookmarkIn = aBookmark().withId("11").withUrl("url").addTag("tag").build();

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(post(TABOO_BOOKMARKS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(bookmarkIn)))
                .andExpect(status().isPreconditionFailed())
        ;

        new Verifications() {{
            repository.createBookmark((Bookmark) any);
            times = 0;
        }};
    }

    @Test
    public void createBookmarksWithNoBodyYieldsBadRequest() throws Exception {
        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(post(TABOO_BOOKMARKS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;

        new Verifications() {{
            repository.createBookmark((Bookmark) any);
            times = 0;
        }};
    }

    @Test
    public void findBookmarksWithAllTags() throws Exception {
        Bookmark bookmark = createBookmarks("2").get(0);
        bookmark.addTag("abc");

        new Expectations() {{
            repository.findBookmarksWithTags(Arrays.asList("tag2", "abc"), true);
            result = Collections.singletonList(bookmark);
        }};

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(get(TABOO_BOOKMARKS)
                .param("tag", "tag2", "abc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookmark.getId()))) // id check is enough here
        ;

        new Verifications() {{
            repository.findBookmarksWithTags(Arrays.asList("tag2", "abc"), true);
            times = 1;
        }};
    }

    @Test
    public void findBookmarksWithAnyTag() throws Exception {
        List<Bookmark> bookmarks = createBookmarks("2", "3");
        bookmarks.get(0).addTag("abc");
        bookmarks.get(1).addTag("abc");

        new Expectations() {{
            repository.findBookmarksWithTags(Arrays.asList("tag2", "abc"), false);
            result = bookmarks;
        }};

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(get(TABOO_BOOKMARKS)
                .param("tag", "tag2", "abc")
                .param("op", "or")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(bookmarks.get(0).getId()))) // id check is enough here
                .andExpect(jsonPath("$[1].id", is(bookmarks.get(1).getId())))
        ;

        new Verifications() {{
            repository.findBookmarksWithTags(Arrays.asList("tag2", "abc"), false);
            times = 1;
        }};
    }

    @Test
    public void findExistingBookmark() throws Exception {
        final Bookmark bookmark = createBookmarks("11").get(0);
        new Expectations() {{
            repository.findBookmarkById("11");
            result = bookmark;
        }};

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(get("/taboo/bookmarks/{id}", bookmark.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookmark.getId())))
                .andExpect(jsonPath("$.url", is(bookmark.getUrl())))
                .andExpect(jsonPath("$.tags[0]", is(bookmark.getTags().iterator().next())))
        ;

        new Verifications() {{
            repository.findBookmarkById("11");
            times = 1;
        }};
    }

    @Test
    public void findNotExistingBookmarkYieldsNotFound() throws Exception {
        new Expectations() {{
            repository.findBookmarkById("11");
            result = new NotFoundException("bookmark 11");
        }};

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(get("/taboo/bookmarks/{id}", 11).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;

        new Verifications() {{
            repository.findBookmarkById("11");
            times = 1;
        }};
    }

    @Test
    public void getAllBookmarks() throws Exception {
        List<Bookmark> bookmarks = createBookmarks("23", "42");

        new Expectations() {{
            repository.findAllBookmarks();
            result = bookmarks;
        }};

        MockMvc mockMvc = standaloneSetup(tabooService).build();
        mockMvc.perform(get(TABOO_BOOKMARKS).accept(MediaType.APPLICATION_JSON))
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

    /**
     * helper method to create a list of bookmarks.
     *
     * @param ids
     *         id values for the Bookmark objects to create
     * @return a list of bookmarks
     */
    private List<Bookmark> createBookmarks(String... ids) {
        List<Bookmark> bookmarks = new ArrayList<>();
        for (String id : ids) {
            bookmarks.add(aBookmark().withId(id).withUrl("url" + id).addTag("tag" + id).build());
        }
        return bookmarks;
    }
}
