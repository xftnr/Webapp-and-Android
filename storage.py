from google.appengine.ext import ndb
# from google.appengine.api import taskqueue
from google.appengine.ext.blobstore import blobstore

import os
import urllib
import jinja2
import webapp2

from google.appengine.api import users

#main model for each post
class Stmessage (ndb.Model):
    email = ndb.StringProperty(indexed=False)
    # author = ndb.StructuredProperty(Author)
    create_time = ndb.DateTimeProperty(auto_now_add= True)
    # should be a list, allow one user post multiple pic in one post
    # image = ndb.StructuredProperty(Image)

    title = ndb.StringProperty(required = True)
    content = ndb.StringProperty(indexed = False)
    theme = ndb.StringProperty(required = True)
    # May need change to list, one post can have multiple tags
    # tag = ndb.StringProperty(index = False)

# class Author(ndb.Model):
    #anonymous number
    # nickname = ndb.StringProperty(required = True)
    # email = ndb.StringProperty(indexed=False)

# class Image(ndb.model):
#     img = ndb.BlobKeyProperty(required = True, indexed = False)


# def createPost(useremail, title, content, theme):
#     message = Stmessage(author = Author(useremail), title = title, content = content, theme = theme)
#     message.put()


JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class IndexPage(webapp2.RequestHandler):

    def get(self):
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        template_values = {
            'user': user,
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('index.html')
        self.response.write(template.render(template_values))

app = webapp2.WSGIApplication([
        ('/', IndexPage),
], debug=True)
