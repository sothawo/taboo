package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;
import static org.hamcrest.CoreMatchers.is;
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
        final Collection<Bookmark> bookmarks = new ArrayList<Bookmark>();
        bookmarks.add(aBookmark().withUrl("url1").addTag("tag1").build());
        bookmarks.add(aBookmark().withUrl("url2").addTag("tag2").build());

        new Expectations() {{
            repository.findAllBookmarks();
            result = bookmarks;
        }};

        MockMvc mockMvc = standaloneSetup(new TabooService(repository)).build();
        mockMvc.perform(get("/taboo/bookmark"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url", is("url1")))
                .andExpect(jsonPath("$[0].tags[0]", is("tag1")))
                .andExpect(jsonPath("$[1].url", is("url2")))
                .andExpect(jsonPath("$[1].tags[0]", is("tag2")))
        ;

        new Verifications() {{
            repository.findAllBookmarks();
            times = 1;
        }};
    }
}
