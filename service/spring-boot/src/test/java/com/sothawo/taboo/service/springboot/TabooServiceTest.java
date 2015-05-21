package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

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
        final Bookmark bookmark1 = aBookmark().withUrl("url1").addTag("tag1").build();
        final Bookmark bookmark2 = aBookmark().withUrl("url2").addTag("tag2").build();

        new Expectations() {{
            repository.findAllBookmarks();
            result = Arrays.asList(bookmark1, bookmark2);
        }};

        MockMvc mockMvc = standaloneSetup(new TabooService(repository)).build();
        mockMvc.perform(get("/taboo/bookmark"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url", is(bookmark1.getUrl())))
                .andExpect(jsonPath("$[0].tags[0]", is(bookmark1.getTags().iterator().next())))
                .andExpect(jsonPath("$[1].url", is(bookmark2.getUrl())))
                .andExpect(jsonPath("$[1].tags[0]", is(bookmark2.getTags().iterator().next())))
        ;

        new Verifications() {{
            repository.findAllBookmarks();
            times = 1;
        }};
    }
}
