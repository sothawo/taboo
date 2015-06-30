/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import com.sothawo.taboo.common.Bookmark;
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

    /** taboo url */
    private String tabooUrl;

    /** taboo url for bookmarks */
    private String tabooUrlBookmarks;

    /** taboo url for tags */
    private String tabooUrlTags;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface TabooClient ---------------------

    /**
     * deletes the given bookmark.
     *
     * @param bookmark
     *         the bookmark to delete
     */
    @Override
    public void deleteBookmark(Bookmark bookmark) {
        RestTemplate rest = new RestTemplate();
        rest.delete(tabooUrlBookmarks + "/" + bookmark.getId());
    }

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
        if (null != tags && !tags.isEmpty()) {
            queryParam = '?' + tags.stream().map(tag -> "tag=" + tag).collect(Collectors.joining("&"));
        }

        String url = tabooUrlBookmarks + queryParam;
        logger.debug("get Bookmarks: {}", url);
        ResponseEntity<Bookmark[]> response = rest.getForEntity(url, Bookmark[].class);

        HttpStatus status = response.getStatusCode();
        logger.debug("get bookmarks: {} {}", status.toString(), status.getReasonPhrase());

        return Arrays.asList(response.getBody());
    }

    /**
     * retrieves all bookmarks that match a given title.
     *
     * @param title
     *         the title to match
     * @return matching bookmarks
     */
    @Override
    public Collection<Bookmark> getBookmarks(String title) {
        RestTemplate rest = new RestTemplate();
        String url = tabooUrlBookmarks + "?title={title}";
        logger.debug("get Bookmarks: {}", url);
        ResponseEntity<Bookmark[]> response = rest.getForEntity(url, Bookmark[].class, title);

        HttpStatus status = response.getStatusCode();
        logger.debug("get bookmarks: {} {}", status.toString(), status.getReasonPhrase());

        return Arrays.asList(response.getBody());
    }

    /**
     * get all the tags available in the service.
     *
     * @return
     */
    @Override
    public Collection<String> getTags() {
        RestTemplate rest = new RestTemplate();
        logger.debug("get tags: {}", tabooUrlTags);
        ResponseEntity<String[]> response = rest.getForEntity(tabooUrlTags, String[].class);
        HttpStatus status = response.getStatusCode();
        logger.debug("get bookmarks: {} {}", status.toString(), status.getReasonPhrase());

        return Arrays.asList(response.getBody());
    }

    /**
     * @param bookmark
     *         the bookmark to save
     */
    @Override
    public void storeNewBookmark(Bookmark bookmark) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Bookmark> response = rest.postForEntity(tabooUrlBookmarks, bookmark, Bookmark.class);
        logger.debug("stored bookmark: {}", response.toString());
    }

// -------------------------- OTHER METHODS --------------------------

    @PostConstruct
    public void init() {
        tabooUrl = properties.getHost().getProtocol() + "://" + properties.getHost().getName() + ':'
                + properties.getHost().getPort() + '/' + properties.getContext();
        logger.debug("taboo url: {}", tabooUrl);
        tabooUrlBookmarks = tabooUrl + "/bookmarks";
        tabooUrlTags = tabooUrl + "/tags";
    }
}
