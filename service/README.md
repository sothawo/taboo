# service module

this moudle contains the different REST) service implementations of the taboo service. 

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
