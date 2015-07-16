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
    this.selectedTags = new TabooSet();
    this.availableTags = new TabooSet();

    /**
     * clears all selection data and loads the bookmarks for no selection.
     */
    this.clearSelection = function () {
        // clear the search field
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
        for(var i = 0, tot = that.bookmarks.length; i < tot; i++) {
            var bookmark = that.bookmarks[i];
            tags = tags.union(bookmark.tags);
        }

        // remove the selected tags and set the result as available
        that.availableTags = tags.difference(that.selectedTags);
    };

    /**
     * reloads the bookmarks for the given selection criteria
     */
    this.reloadBookmarks = function () {
        // TODO: search parameters
        $http.get("http://localhost:8081/taboo/bookmarks")
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
