/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkBuilder;
import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.sothawo.taboo.common.BookmarkBuilder.aBookmark;

/**
 * REST Client for a taboo service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@SpringComponent
@EnableConfigurationProperties(TabooRestClientConfigurationProperties.class)
public class TabooRestClient implements TabooClient {
// ------------------------------ FIELDS ------------------------------

    /** Logger */
    private final static Logger logger = LoggerFactory.getLogger(TabooClient.class);

    @Autowired
    private TabooRestClientConfigurationProperties properties;

    private String tabooUrl;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface TabooClient ---------------------

    /**
     * retrieves all bookmarks with the given tags
     *
     * @param tags
     *         the tags of the bookmarks
     * @return list of bookmarks
     */
    @Override
    public Collection<Bookmark> getBookmarks(Collection<String> tags) {
        RestTemplate rest = new RestTemplate();
        // need to build the arguments as string as they have the same name
        String queryParam = "";
        if (null != tags) {
            queryParam = '?' + tags.stream().map(tag -> "tag=" + tag).collect(Collectors.joining("&"));
        }

        String url = tabooUrl + "/bookmarks" + queryParam;
        logger.debug("get Bookmarks: {}", url);
        ResponseEntity<Bookmark[]> response = rest.getForEntity(url, Bookmark[].class);

        HttpStatus status = response.getStatusCode();
        logger.debug("get bookmarks: {} {}", status.toString(), status.getReasonPhrase());

        return Arrays.asList(response.getBody());
    }

    /**
     * @param url
     *         the url of the bookmark
     * @param tags
     */
    @Override
    public void storeNewBookmark(String url, Collection<String> tags) {
        BookmarkBuilder bookmarkBuilder = aBookmark().withUrl(url);
        tags.stream().filter(s -> !s.isEmpty()).forEach(bookmarkBuilder::addTag);

        RestTemplate rest = new RestTemplate();
        ResponseEntity<Bookmark> response =
                rest.postForEntity(tabooUrl + "/bookmarks", bookmarkBuilder.build(), Bookmark.class);
        logger.debug("stored bookmark: {}", response.toString());
    }

// -------------------------- OTHER METHODS --------------------------

    @PostConstruct
    public void init() {
        tabooUrl = properties.getHost().getProtocol() + "://" + properties.getHost().getName() + ':'
                + properties.getHost().getPort() + '/' + properties.getContext();
        logger.debug("taboo url: {}", tabooUrl);
    }
}
