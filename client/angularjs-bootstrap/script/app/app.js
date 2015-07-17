/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */

var app = angular.module('taboo', []);

// create Controller that creates a ViewModel
app.controller('BookmarksCtrl', function ($scope, $http) {
    $scope.vm = new BookmarksVM($http);
});


// ViewModel
function BookmarksVM($http) {
    var that = this;
    this.bookmarks = [];
    this.searchText = "";
    this.selectedTags = new TabooSet();
    this.availableTags = new TabooSet();

    /**
     * clears all selection data and loads the bookmarks for no selection.
     */
    this.clearSelection = function () {
        that.searchText = "";
        that.setSelectedTags([]);
    }

    /**
     * sets the selected tags and loads the bookmarks for the tags.
     * @param tags the new selected tags
     */
    this.setSelectedTags = function (tags) {
        if (tags.isSetObject) {
            that.selectedTags = tags;
        } else if (angular.isArray(tags)) {
            that.selectedTags = new TabooSet(tags);
        } else {
            that.selectedTags = new TabooSet();
        }
        that.reloadBookmarks();
    }

    /**
     * sets the bookmarks to show and updates the tag sets.
     * @param bookmarks
     */
    that.setBookmarksToShow = function (bookmarks) {
        that.bookmarks = bookmarks;
        // collect the tags from the bookmarks
        var tags = new TabooSet();
        var count = that.bookmarks.length;
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var bookmark = that.bookmarks[i];
                tags = tags.union(bookmark.tags);
            }

            // remove the selected tags and set the result as available
            that.availableTags = tags.difference(that.selectedTags);
        }
    };

    /**
     * reloads the bookmarks for the given selection criteria
     */
    this.reloadBookmarks = function () {
        // search parameters
        var paramsAreSet = false;
        var params = {};
        if (that.selectedTags.size() > 0) {
            params["tag"] = that.selectedTags.getElements();
            paramsAreSet = true;
        }
        if (that.searchText) {
            params["search"] = that.searchText;
            paramsAreSet = true;
        }

        if (paramsAreSet) {
            $http.get("http://localhost:8081/taboo/bookmarks", {params: params})
                .then(function (result) {
                    var bookmarks = [];
                    var i = 0;
                    while (i < result.data.length) {
                        var bookmark = new Bookmark(result.data[i]);
                        bookmarks.push(bookmark);
                        i++;
                    }
                    that.setBookmarksToShow(bookmarks);
                }).catch(function (result) {
                    alert("Fehler: " + result.status + " " + result.statusText);
                });
        } else {
            // only get the tags
            $http.get("http://localhost:8081/taboo/tags")
                .then(function (result) {
                    that.setBookmarksToShow([]);
                    that.availableTags = new TabooSet(result.data);
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
        that.selectedTags.add(tag);
        that.reloadBookmarks();
    };

    /**
     * removes a tag from the selected tags list and reloads the bookmarks.
     * @param tag
     */
    this.removeTagFromSelection = function (tag) {
        that.selectedTags.remove(tag);
        that.reloadBookmarks();
    };

    //initial setup
    that.clearSelection();
}


// Bookmark class
function Bookmark(other) {
    this.id = other.id;
    this.url = other.url;
    this.title = other.title;
    this.tags = new TabooSet(other.tags);

    this.joinedTags = function () {
        return this.tags.getElements().join(', ');
    };
}
