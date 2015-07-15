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
    this.selectedTagList = [];

    that.bookmarks.push(
        new Bookmark('1', 'www.b1.de', 'b1', ['tag1-1', 'tag-1-2']),
        new Bookmark('2', 'www.b2.de', 'b2', ['tag2-1', 'tag-2-2'])
    );

    /**
     * clears all selection data
     */
    this.clearSelection = function() {
        // clear the search field
        that.setSelectedTags([]);
    }

    /**
     * sets the selected tags and loads the bookmarks for the tags.
     * @param tags the new selected tags
     */
    this.setSelectedTags = function(tags) {
        if(Array.isArray(tags)) {
            that.selectedTagList = tags;
        } else {
            that.selectedTagList = [];
        }
        that.reloadBookmarks();
    }

    /**
     * reloads the bookmarks for the given selection criteria
     */
    this.reloadBookmarks = function() {
        $http.get("http://localhost:8081/taboo/bookmarks")
            .then(function(result){
                that.bookmarks = result.data;
            }).catch(function(result){
                alert("Fehler: " + result.status + " " + result.statusText);
            });
    }

    //initial setup
    that.clearSelection();
}

// Bookmark class
function Bookmark(id, url, title, tags) {
    this.id = id;
    this.url = url;
    this.title = title;
    this.tags = tags;
}
