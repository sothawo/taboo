# service module

this moudle contains the different REST) service implementations of the taboo service. 

It does not contain the implementation of repositories where the bookmark data is stored,
this is in the _repositories_ module.

## service REST calls

the following calls are implemented by the service:

### bookmarks

#### _GET /taboo/bookmark_ 
  
gets all bookmarks from the repository

#### _GET /taboo/bookmark/{id}_

gets the bookmark with the given id 


### tags

#### _GET /taboo/tag

gets all known tags
