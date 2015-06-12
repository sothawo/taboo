# service module

this module contains the different (REST) service implementations of the taboo service. Data format for input and 
output ist JSON.

It does not contain the implementation of repositories where the bookmark data is stored,
this is in the _repositories_ module.

## service REST calls

the following calls are implemented by the service:

### bookmarks

#### _GET /taboo/bookmarks_ 
  
gets all bookmarks from the repository

#### _GET /taboo/bookmarks/{id}_

gets the bookmark with the given id 

#### _GET /taboo/bookmarks?tag=t1&tag=t2_

gets all bookmarks that have all of the given tags, tag parameter can be used multiple times
 
#### _GET /taboo/bookmarks?op=or&?tag=t1&tag=t2_

gets all bookmarks that have any of the given tags, tag parameter can be used multiple times
 
#### _POST /taboo/bookmarks_

creates a new bookmark. the bookmark data must be sent as JSON data in the body. The id of the bookmark must not be 
set.

#### _GET/taboo/tags_

gets all tags from the repository (no duplicates)
