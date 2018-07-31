from google.appengine.ext import ndb
# from google.appengine.api import taskqueue
from google.appengine.ext.blobstore import blobstore

import webapp2

#main model for each post
class Stmessage (ndb.Model):
    author = ndb.StructuredProperty(Author)
    create_time = ndb.DateTimeProperty(auto_now_add= True)
    # should be a list, allow one user post multiple pic in one post
    # image = ndb.StructuredProperty(Image)

    title = ndb.StringProperty(required = True)
    content = ndb.StringProperty(indexed = False)
    theme = ndb.StringProperty(required = True)
    # May need change to list, one post can have multiple tags
    # tag = ndb.StringProperty(index = False)

class Author(ndb.Model):
    #anonymous number
    # nickname = ndb.StringProperty(required = True)
    email = ndb.StringProperty(indexed=False)

class Image(ndb.model):
    img = ndb.BlobKeyProperty(required = True, indexed = False)

#return a key
def getkey():
    return ndb.Key("woodo", "default_key")

def createPost(useremail, title, content, theme):
    message = Stmessage(parent = getkey(), author = Author(useremail), title = title, content = content, theme = theme)
    message.put()
    return message
