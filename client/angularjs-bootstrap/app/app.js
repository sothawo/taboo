/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */

var app = angular.module('taboo', []);

app.controller('BookmarksCtrl', function ($scope, $http) {
    $scope.vm = new BookmarksVM($http);
});

function BookmarksVM($http) {
    var that = this;
    this.bookmarks = new Array();

    that.bookmarks.push(
        new Bookmark('1', 'www.b1.de', 'b1', ['tag1-1', 'tag-1-2']),
        new Bookmark('2', 'www.b2.de', 'b2', ['tag2-1', 'tag-2-2'])
    );
}


function Bookmark(id, url, title, tags) {
    this.id = id;
    this.url = url;
    this.title = title;
    this.tags = tags;
}
