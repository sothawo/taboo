/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import com.sothawo.taboo.common.AlreadyExistsException;
import com.sothawo.taboo.common.Bookmark;
import com.sothawo.taboo.common.BookmarkRepository;
import com.sothawo.taboo.common.NotFoundException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Spring-Boot Service implementation for the taboo backend service.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@RestController // contains @ResponseBody
@RequestMapping("/taboo")
public class TabooService {
    // ------------------------------ FIELDS ------------------------------
    /** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(TabooService.class);

    /** needed for tests. */
    static final String MAGIC_TEST_URL = "magicTestStringThatsNotAnUrl";

    /** OR operation. */
    private static final String OP_OR = "or";
    /** AND operation. */
    private static final String OP_AND = "and";

    /** the repository for the bookmarks, injected in constructor or explicitly set in no-arg-ctor. */
    private BookmarkRepository repository;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * create a service and use a hardcoded repository.
     */
    public TabooService() {
        repository = Application.getSpringMongoRepository();
    }

    /**
     * create a Service with a given repository.
     *
     * @param repository
     *         the repository to use
     */
    public TabooService(final BookmarkRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * creates a new bookmark in the repository.
     *
     * @param bookmark
     *         new bookmark to be created
     * @param ucb
     *         uri component builder to build the created uri
     * @return the created bookmark
     * @throws IllegalArgumentException
     *         when bookmarkis null or has it's id set
     */
    @RequestMapping(value = "/bookmarks", method = RequestMethod.POST)
    public final ResponseEntity<Bookmark> createBookmark(@RequestBody final Bookmark bookmark,
                                                         final UriComponentsBuilder ucb) {
        if (null == bookmark) {
            throw new IllegalArgumentException("bookmark must not be null");
        }
        if (null != bookmark.getId()) {
            throw new IllegalArgumentException("id must not be set");
        }

        Bookmark createdBookmark = repository.createBookmark(bookmark);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb
                .path("/taboo/bookmarks/")
                .path(String.valueOf(createdBookmark.getId()))
                .build().toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(createdBookmark, headers, HttpStatus.CREATED);
    }

    /**
     * deletes a bookmark from the repository.
     *
     * @param id
     *         id of the bookmark to delete
     * @throws NotFoundException
     *         when no Bookmarks is found for the id
     */
    @RequestMapping(value = "/bookmarks/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final void deleteBookmarkById(@PathVariable(value = "id") final String id) {
        repository.deleteBookmark(id);
    }

    /**
     * ExceptionHandler for AlreadyExistsException. returns the exception's error message in the body with the 409
     * status code.
     *
     * @param e
     *         the exception to handle
     * @return HTTP CONFLICT Response Status and error message
     */
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final String exceptionHandlerAlreadyExistsException(final AlreadyExistsException e) {
        return e.getMessage();
    }

    /**
     * ExceptionHandler for IllegalArgumentException. returns the exception's error message in the body with the 412
     * status code.
     *
     * @param e
     *         the exception to handle
     * @return HTTP PRECONDITION_FAILED Response Status and error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public final String exceptionHandlerIllegalArgumentException(final IllegalArgumentException e) {
        return e.getMessage();
    }

    /**
     * ExceptionHandler for NotFoundException. returns the exception's error message in the body with the 404 status
     * code.
     *
     * @param e
     *         the exception to handle
     * @return HTTP NOT_FOUND Response Status and error message
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final String exceptionHandlerNotFoundException(final NotFoundException e) {
        return e.getMessage();
    }

    /**
     * gets the bookmarks from the repository. There can be additional selection citeria, either tags or a search search
     * string.
     *
     * @param tags
     *         optional list of tags
     * @param op
     *         if "or", tags are combined with OR, otherwise with AND
     * @param search
     *         optional search string to be searched
     * @return all bookmarks
     */
    @RequestMapping(value = "/bookmarks", method = RequestMethod.GET)
    public final Collection<Bookmark> findAllBookmarks(@RequestParam(value = "tag", required = false)
                                                       final List<String> tags,
                                                       @RequestParam(value = "op", defaultValue = OP_AND)
                                                       final String op,
                                                       @RequestParam(value = "search", required = false)
                                                       final String search) {
        boolean opAnd = !OP_OR.equals(op.toLowerCase());
        if (null == tags && null == search) {
            return repository.getAllBookmarks();
        } else if (null == search) {
            // only tags
            return repository.getBookmarksWithTags(tags, opAnd);
        } else if (null == tags) {
            // only search
            return repository.getBookmarksWithSearch(search);
        } else {
            return repository.getBookmarksWithTagsAndSearch(tags, opAnd, search);
        }
    }

    /**
     * return all tags from the repository.
     *
     * @return collection of tags
     */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public final Collection<String> findAlltags() {
        return repository.getAllTags().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * returns the bookmark with the given id.
     *
     * @param id
     *         id of the bookmark
     * @return Bookmark
     * @throws NotFoundException
     *         when no Bookmarks is found for the id
     */
    @RequestMapping(value = "/bookmarks/{id}", method = RequestMethod.GET)
    public final Bookmark findBookmarkById(@PathVariable(value = "id") final String id) {
        return repository.getBookmarkById(id);
    }

    /**
     * updates a bookmark in the repository.
     *
     * @param bookmark
     *         bookmark to be updated
     * @throws IllegalArgumentException
     *         when bookmarkis null or doesnt have it's id set
     */
    @RequestMapping(value = "/bookmarks", method = RequestMethod.PUT)
    public final void updateBookmark(@RequestBody final Bookmark bookmark) {
        if (null == bookmark) {
            throw new IllegalArgumentException("bookmark must not be null");
        }
        if (null == bookmark.getId()) {
            throw new IllegalArgumentException("id must be set");
        }
        repository.updateBookmark(bookmark);
    }

    /**
     * tries to load the title for a web page.
     *
     * @param url
     *         url for which the title shall be loaded
     * @return ResponseEntity with the title
     */
    @RequestMapping(value = "/title", method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public final ResponseEntity<String> loadTitle(@RequestParam(value = "url", required = true) final String url) {
        if (MAGIC_TEST_URL.equals(url)) {
            return new ResponseEntity<>(url, HttpStatus.OK);
        }

        String urlString = url;
        if (null != urlString && !urlString.isEmpty()) {
            if (!urlString.startsWith("http")) {
                urlString = "http://" + urlString;
            }
            final String finalUrl = urlString;
            logger.debug("loading title for url {}", finalUrl);
            try {
                String htmlTitle = Jsoup.connect(finalUrl).timeout(5000).get().title();
                logger.debug("got title: {}", htmlTitle);
                return new ResponseEntity<>(htmlTitle, HttpStatus.OK);
            } catch (HttpStatusException e) {
                logger.info("loading url http error", e);
                return new ResponseEntity<>(HttpStatus.valueOf(e.getStatusCode()));
            } catch (IOException e) {
                logger.info("loading url error", e);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
