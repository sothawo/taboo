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

    /**
     * clears all selection data and loads the bookmarks for no selection.
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
        if(angular.isArray(tags)) {
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
                that.bookmarks = [];
                var i = 0;
                while( i < result.data.length){
                    var bookmark = new Bookmark(result.data[i]);
                    that.bookmarks.push(bookmark);
                    i++;
                }
            }).catch(function(result){
                alert("Fehler: " + result.status + " " + result.statusText);
            });
    }

    //initial setup
    that.clearSelection();
}

// Bookmark class
function Bookmark(other) {
    this.id = other.id;
    this.url = other.url;
    this.title = other.title;
    this.tags = other.tags;
    this.joinedTags = this.tags.join(', ');
}
