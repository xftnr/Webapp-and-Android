#!/usr/bin/env python


# [START imports]
import os
import urllib

from google.appengine.api import users
from google.appengine.ext import ndb
from google.appengine.api import images


#from flask import Flask, flash



import jinja2
import webapp2



JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)
# [END imports]

DEFAULT_APP_NAME = 'default_woodo'


# We set a parent key on the 'Greetings' to ensure that they are all
# in the same entity group. Queries across the single entity group
# will be consistent. However, the write rate should be limited to
# ~1/second.

def woodo_key(woodo_name=DEFAULT_APP_NAME):
    """Constructs a Datastore key for a Woodo entity.

    We use woodo_name as the key.
    """
    return ndb.Key('Woodo', woodo_name)


# [START Author]
class Author(ndb.Model):
    """Sub model for representing an author."""
    identity = ndb.StringProperty(indexed=False)
    email = ndb.StringProperty(indexed=False)
    nickname = ndb.StringProperty(indexed=False)




class Posts(ndb.Model):
    """A main model for representing an individual Post entry."""
    author = ndb.StructuredProperty(Author)
    title = ndb.StringProperty(indexed=False)
    categories = ndb.StringProperty(indexed=False)
    content = ndb.StringProperty(indexed=False)
    date = ndb.DateTimeProperty(auto_now_add=True)
    istrue = ndb.BooleanProperty(indexed=True)
    post_image = ndb.BlobProperty()
    title_lower = ndb.ComputedProperty(lambda self: self.title.lower())
    categories_lower = ndb.ComputedProperty(lambda self: self.categories.lower())
    content_lower = ndb.ComputedProperty(lambda self: self.content.lower())
# [END Author]


# [START main_page]
class MainPage(webapp2.RequestHandler):

    def get(self):
        
        woodo_name = self.request.get('woodo_name',
                                          DEFAULT_APP_NAME)

        search_string = self.request.get('s').lower()
        limit = ""
        posts_query = Posts.query(ancestor=woodo_key(woodo_name))

        if (search_string):
            limit = search_string[:-1] + chr(ord(search_string[-1]) + 1)
            posts_query = posts_query.filter(
                ndb.OR(
                    ndb.AND(Posts.title_lower >= search_string, Posts.title_lower < limit),
                    ndb.AND(Posts.categories_lower >= search_string, Posts.categories_lower < limit),
                    ndb.AND(Posts.content_lower >= search_string, Posts.content_lower < limit),
                )

            )
        else:
            posts_query = posts_query.order(-Posts.date)
        posts = posts_query.fetch(50)

        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

       
        template_values = {
            'user': user,
            'posts': posts,
            'woodo_name': urllib.quote_plus(woodo_name),
            'url': url,
            'url_linktext': url_linktext
        }
        
      
        template = JINJA_ENVIRONMENT.get_template('index.html')
        self.response.write(template.render(template_values))
# [END main_page]


# [START post_page]
class PostPage(webapp2.RequestHandler):   

    def get(self):
        woodo_name = self.request.get('woodo_name',
                                          DEFAULT_APP_NAME)
        user = users.get_current_user()
        url=""
        url_linktext=""
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            self.redirect('/')

       
        template_values = {
            'user': user,
            'url': url,
            'woodo_name': urllib.quote_plus(woodo_name),
            'url_linktext': url_linktext
        }								  
										  
        template = JINJA_ENVIRONMENT.get_template('post.html')
        self.response.write(template.render(template_values))
# [END post_page]


# [START woodo]
class Woodo(webapp2.RequestHandler):

    def post(self):
        # We set the same parent key on the 'post' to ensure each
        # Post is in the same entity group. Queries across the
        # single entity group will be consistent. However, the write
        # rate to a single entity group should be limited to
        # ~1/second.
        woodo_name = self.request.get('woodo_name',
                                          DEFAULT_APP_NAME)
        post = Posts(parent=woodo_key(woodo_name))

        if users.get_current_user():
            post.author = Author(
                    identity=users.get_current_user().user_id(),
                    email=users.get_current_user().email(),
                    nickname = users.get_current_user().nickname())
        post.title = self.request.get('title')
        categories = ', '.join(str(e) for e in self.request.params.getall('categories'))
        post.categories = categories
        post.content = self.request.get('content')
        postImage = self.request.get('post_image')
        postImage = images.resize(postImage,200,250)
        post.post_image=postImage
        post.put()

        query_params = {'woodo_name': woodo_name}
        #self.redirect('/?' + urllib.urlencode(query_params))
       
        self.redirect('/')
# [END woodo]

# [Start Image]
class Image(webapp2.RequestHandler):
    def get(self):
        post_key=ndb.Key(urlsafe=self.request.get('img_id'))
        post= post_key.get()
        if post.post_image:
            self.response.headers['Content-Type'] = 'img/png'
            self.response.out.write(post.post_image)
        else:
            self.response.out.write('No image')

#[END Image]

# [START app]
app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/newpost', PostPage),
    ('/submit', Woodo),
    ('/myimg',Image),
], debug=True)
# [END app]
