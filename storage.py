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
    loader=jinja2.FileSystemLoader('Front-End'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# class MainHandler(webapp2.RequestHandler):
#     def get(self):
#         # template = JINJA_ENVIRONMENT.get_template('/Front-End/coverpage.html')
#         self.response.write(/Front-End/coverpage.html)

# class MainPage(webapp2.RequestHandler):
#     def get(self):
#         self.redirect('/Front-End/coverpage.html', permanent=True)

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

class MainPage(webapp2.RequestHandler):
    def get(self):
         # template_values = {
         #     # 'user': user,
         #     'url': url,
         #     # 'url_linktext': url_linktext,
         # }

         template = JINJA_ENVIRONMENT.get_template('coverpage.html')
         self.response.write(template.render())

class PostPage(webapp2.RequestHandler):
    def get(self):
        # need to get category, tag
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

         template = JINJA_ENVIRONMENT.get_template('post.html')
         self.response.write(template.render(template_values))

    def post(self):



app = webapp2.WSGIApplication([
        ('/', MainPage),
        ('/index.html', IndexPage),
        ('/post.html', PostPage),
], debug=True)
