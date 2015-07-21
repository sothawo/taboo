/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */

var app = angular.module('taboo', []);

// set configuration for the backend service
app.constant('tabooService', {
    urlService: 'http://localhost:8081',
    pathBookmarks: '/taboo/bookmarks',
    pathTags: '/taboo/tags',
    pathTitle: '/taboo/title'
});

// create Controller that creates a ViewModel
app.controller('TabooCtrl', function ($scope, $http, tabooService) {
    $scope.vm = new TabooVM($http, tabooService);
});


// ViewModel
function TabooVM($http, tabooService) {
    var self = this;
    /** entry for new bookmark url. */
    this.newBookmarkUrl = "";
    /** entry for new bookmark title. */
    this.newBookmarkTitle = "";
    /** the bookmarks to show. */
    this.bookmarks = [];
    /** search field for bookmarks. */
    this.searchText = "";
    /** the list of selected tags. */
    this.selectedTags = new TabooSet();
    /** the list of available tags */
    this.availableTags = new TabooSet();

    /**
     * clears all selection data and ses the selected tags to empty.
     */
    this.clearSelection = function () {
        self.searchText = "";
        self.setSelectedTags([]);
    }

    /**
     * sets the selected tags and reloads the bookmarks for the tags.
     * @param tags the new selected tags.
     */
    this.setSelectedTags = function (tags) {
        if (tags.isSetObject) {
            self.selectedTags = tags;
        } else if (angular.isArray(tags)) {
            self.selectedTags = new TabooSet(tags);
        } else {
            self.selectedTags = new TabooSet();
        }
        self.reloadBookmarks();
    }

    /**
     * sets the bookmarks to show and updates the tag sets.
     * @param bookmarks
     */
    self.setBookmarksToShow = function (bookmarks) {
        self.bookmarks = bookmarks;
        // collect the tags from the bookmarks
        var tags = new TabooSet();
        var count = self.bookmarks.length;
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var bookmark = self.bookmarks[i];
                tags = tags.union(bookmark.tags);
            }

            // remove the selected tags and set the result as available
            self.availableTags = tags.difference(self.selectedTags);
        }
    };

    /**
     * reloads the bookmarks for the given selection criteria
     */
    this.reloadBookmarks = function () {
        // search parameters
        var paramsAreSet = false;
        var params = {};
        if (self.selectedTags.size() > 0) {
            params["tag"] = self.selectedTags.getElements();
            paramsAreSet = true;
        }
        if (self.searchText) {
            params["search"] = self.searchText;
            paramsAreSet = true;
        }

        if (paramsAreSet) {
            $http.get(tabooService.urlService + tabooService.pathBookmarks, {params: params})
                .then(function (result) {
                    var bookmarks = [];
                    var i = 0;
                    while (i < result.data.length) {
                        var bookmark = new Bookmark(result.data[i]);
                        bookmarks.push(bookmark);
                        i++;
                    }
                    self.setBookmarksToShow(bookmarks);
                }).catch(function (result) {
                    alert("Fehler: " + result.status + " " + result.statusText);
                });
        } else {
            // only get the tags
            $http.get(tabooService.urlService + tabooService.pathTags)
                .then(function (result) {
                    self.setBookmarksToShow([]);
                    self.availableTags = new TabooSet(result.data);
                }).catch(function (result) {
                    alert("Fehler: " + result.status + " " + result.statusText);
                });
        }
    };

    /**
     * adds a tag to the selected tag list and reloads the bookmarks.
     * @param tag
     */
    this.addTagToSelection = function (tag) {
        self.selectedTags.add(tag);
        self.reloadBookmarks();
    };

    /**
     * removes a tag from the selected tags list and reloads the bookmarks.
     * @param tag
     */
    this.removeTagFromSelection = function (tag) {
        self.selectedTags.remove(tag);
        self.reloadBookmarks();
    };

    /**
     * tries to load the title for current new bookmark url.
     */
    this.loadTitle = function () {
        if (self.newBookmarkUrl) {
            $http.get(tabooService.urlService + tabooService.pathTitle, {params: {url: self.newBookmarkUrl}})
                .then(function (result) {
                    self.newBookmarkTitle = result.data;
                }).catch(function (result) {
                    alert("Fehler: " + result.status + " " + result.statusText);
                });
        }
    };

    //initial setup
    self.clearSelection();
}


/**
 * creates a Bookmark from a REST-data Bookmark (these have an array of tags, not a Set).
 * @param restBookmark bookmark from a REST call.
 * @constructor
 */
function Bookmark(restBookmark) {
    this.id = restBookmark.id;
    this.url = restBookmark.url;
    this.title = restBookmark.title;
    this.tags = new TabooSet(restBookmark.tags);

    this.joinedTags = function () {
        return this.tags.getElements().join(', ');
    };
}
