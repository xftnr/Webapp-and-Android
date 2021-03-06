from google.appengine.ext import ndb
from google.appengine.ext.blobstore import blobstore

import os
import urllib
import jinja2
import webapp2
from random import randint
import json
import logging



from google.appengine.api import users
from google.appengine.api import images
from google.appengine.api import mail
# android
# from google.oauth2 import id_token
# from google.auth.transport import requests
# import cachecontrol
# import google.auth.transport.requests
# import requests
# images

#main model for each post
class Stmessage (ndb.Model):
    create_time = ndb.DateTimeProperty(auto_now_add= True, indexed = True)
    # should be a list, allow one user post multiple pic in one post
    # change here required
    img = ndb.BlobProperty(required = False)
    title = ndb.StringProperty(required = True, indexed=True)
    content = ndb.StringProperty(indexed = True)
    theme = ndb.StringProperty(required = True, indexed = True)
    title_lower = ndb.ComputedProperty(lambda self: self.title.lower())
    theme_lower = ndb.ComputedProperty(lambda self: self.theme.lower())
    content_lower = ndb.ComputedProperty(lambda self: self.content.lower())
    latitude = ndb.FloatProperty(required=True)
    longitude = ndb.FloatProperty(required=True)


class Author(ndb.Model):
    # anonymous number
    # nickname = ndb.StringProperty(required = True)
    identity = ndb.StringProperty(required = True, indexed=True)
    email = ndb.StringProperty(required = True, indexed=False)
    subscribed = ndb.StringProperty(repeated = True, indexed= True)
    subtest = ndb.ComputedProperty(lambda self: len(self.subscribed) ==0)
    a_postkey = ndb.KeyProperty(kind=Stmessage, repeated = True)


class Tags (ndb.Model):
    tag = ndb.StringProperty(indexed = True)
    # dont only do one to many because I only want to implement the search function
    postskey = ndb.KeyProperty(kind=Stmessage, repeated = True)


JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader('Front-End'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# get tag list
def getlist ():
    all_tags = Tags.query()
    tagslist = []
    for i in all_tags.fetch():
        tagslist.append(i.tag)
    return tagslist


class IndexPage(webapp2.RequestHandler):

    def get(self):
        # get all
        tag_priority = False
        search_string = self.request.get('input-search')
        posts_query = Stmessage.query()
        # all_tags = Tags.query()
        tagslist = getlist()
        # for i in all_tags.fetch():
        #     tagslist.append(i.tag)
        all_tags = Tags.query()
        if search_string:
            tags_query = all_tags.filter(Tags.tag == search_string)
            assert tags_query !=None
            # assert len(tags_query) ==1
            target = tags_query.get()
            # assert target != None
            if target:
                tag_priority = True
                listofpost = []


                for posts_key in target.postskey:
                    listofpost.append(posts_key.get())
            else:

                temp = search_string.lower()
                limit = search_string[:-1] + chr(ord(search_string[-1]) + 1)
                posts_query = posts_query.filter(
                    ndb.OR(
                        # Stmessage.title_lower.IN(temp)
                        ndb.AND(Stmessage.title_lower >= temp, Stmessage.title_lower < limit),
                        ndb.AND(Stmessage.theme_lower >= temp, Stmessage.theme_lower < limit),
                        ndb.AND(Stmessage.content_lower >= temp, Stmessage.content_lower < limit),))

        category = self.request.get('catsearch')
        if category:
            posts_query = Stmessage.query(Stmessage.theme == category )

        if tag_priority:
            posts = listofpost
        else:
            posts = posts_query.order(-Stmessage.create_time).fetch(20)

        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url('/login')
            url_linktext = 'Login'

        template_values = {
            'autotag': tagslist,
            'user': user,
            'posts': posts,
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('index.html')
        self.response.write(template.render(template_values))


class MainPage(webapp2.RequestHandler):
    def get(self):
         template = JINJA_ENVIRONMENT.get_template('coverpage.html')
         self.response.write(template.render())


class PostPage(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()
        if user:
            url = users.create_logout_url('/index')
            url_linktext = 'Logout'
        else:
            self.redirect('/index')


        template_values = {
        # might: json.dumps(taglist)
            # 'taglist': taglist,
            'user': user,
            'url': url,
            'url_linktext': url_linktext,
        }
        template = JINJA_ENVIRONMENT.get_template('post.html')
        self.response.write(template.render(template_values))

    def post(self):
        # new post entity
        post = Stmessage()
        post.title = self.request.get('title')
        temp_cat = self.request.get('category')
        post.theme = temp_cat
        post.content = self.request.get('text')
        upload_images = self.request.get("images")
        post.img = images.resize(upload_images,  height=175)

        listofloc = [
            (30.4474935, -97.8128255),
            (30.4103041, -97.6780196),
            (30.360989, -97.7179217),
            (30.2248315, -97.8385755)
        ]
        pick = randint(0,len(listofloc)-1)
        (post.latitude, post.longitude) = listofloc[pick]
        post_key = post.put()

        user = users.get_current_user()

        # update author entity
        user_account = Author.query( Author.identity == user.user_id()).get()
        # impossible to be none
        # assert user_account == None
        user_account.a_postkey.append(post_key)
        user_account.put()

        # new tag entity or update tag entity
        temp_tag = [x.strip() for x in self.request.get('input_tag').lower().split(",")]
        # store unique tag
        if temp_tag:
            # only first three tag will take
            temp_tag = temp_tag[:3]
            listofentity = []
            for i in temp_tag:
                tags = Tags()
                exist_tag = Tags.query(Tags.tag == i )
                eldertag = exist_tag.get()
                if eldertag is None:
                    tags.tag = i
                    tags.postskey = [post_key]
                    listofentity.append(tags)
                else:
                    eldertag.postskey.append(post_key)
                    listofentity.append(eldertag)
            ndb.put_multi(listofentity)
        self.redirect('/index')


# in index page
class Imagehandler (webapp2.RequestHandler):
    def get(self):
        post_key=ndb.Key(urlsafe=self.request.get('img_id'))
        post= post_key.get()
        if post.img:
            self.response.headers['Content-Type'] = 'image/png'
            self.response.out.write(post.img)
        else:
            self.response.out.write('No image')


class ProfilePage (webapp2.RequestHandler):
    def get(self):

        user = users.get_current_user()
        user_account = Author.query( Author.identity == user.user_id()).get()
        listofpost = []
        listoflon = []
        listoflat = []
        # print(user_account.a_postkey)
        # logging.debug(user_account.a_postkey)
        for i in user_account.a_postkey:
            listofpost.append(i.get())
            listoflat.append({"lat": i.get().latitude, "lon": i.get().longitude})

        # all_tags = Tags.query()
        tagslist = getlist()
        # subscribtion
        subscribedlist = user_account.subscribed
        # get location

        if user:
            url = users.create_logout_url('/index')
            url_linktext = 'Logout'
        else:
            self.redirect('/index')
        # print(listoflat)
        # logging.debug(listoflat)
        template_values = {
            'autotag': json.dumps(tagslist),
            'subscribed': subscribedlist,
            'listofloc': listoflat,
            # 'listlon': json.dumps(listoflon),
            'user': user,
            'posts': listofpost,
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('profile.html')
        self.response.write(template.render(template_values))


class Emailsendhandler(webapp2.RequestHandler):
    def get (self):
        sender_address = "noreply@woodo-apad.appspotmail.com"
        temp_rec = Author.query(Author.subtest == False).fetch()

        for i in temp_rec:
            recivers = i.email

            mail.send_mail(sender=sender_address,
                           to=recivers,
                           subject="Your subscription has new updates",
                           body="""Dear Albert:

        Your subscription has updated. Come and check it out!

        The Woodo Team
        """)

class Loginhandler(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()
        # load the user
        user_account = Author.query( Author.identity == user.user_id()).get()
        if user_account is None:
            new_user = Author()
            new_user.identity = user.user_id()
            new_user.email = user.email()
            new_user.subscribed = []
            new_user.a_postkey = []
            new_user.put()
        # go to index
        self.redirect('/index')


class Subscriptionhandler(webapp2.RequestHandler):
    def post(self):
        user = users.get_current_user()
        user_account = Author.query( Author.identity == user.user_id()).get()
        subscrib = self.request.get('subscrib')
        user_account.subscribed.append(subscrib)
        user_account.put()
        self.redirect('/profile')


class Unsubscriptionhandler(webapp2.RequestHandler):
    def post(self):
        user = users.get_current_user()
        user_account = Author.query( Author.identity == user.user_id()).get()
        unsubscrib = self.request.get('unsubscrib')
        user_account.subscribed.remove(unsubscrib)
        user_account.put()
        self.redirect('/profile')

class Errorpagehandler(webapp2.RequestHandler):
    def get(self):
        template_values = {
            'url': url,
        }
        template = JINJA_ENVIRONMENT.get_template('error.html')
        self.response.write(template)



class MobileGethandler(webapp2.RequestHandler):
    def get(self):
        posts = Stmessage.query().order(-Stmessage.create_time).fetch(10)
        self.response.headers['Content-Type'] = 'application/json'
        posts_for_json = []
        for p in posts:
            post_content = {
                'title': p.title,
                'content': p.content,
                # if i want to use this need to change BlobProperty to blobkeyproperty
                # 'image': images.get_serving_url(p.img)
            }
            posts_for_json.append(post_content)
        self.response.write(json.dumps(posts_for_json))


class MobilePosthandler(webapp2.RequestHandler):
    def post(self):
        # new post entity
        post = Stmessage()
        post.title = self.request.get('title')
        post.theme = self.request.get('category')
        post.content = self.request.get('text')

        # android get the coordinate
        # (post.latitude, post.longitude) = listofloc[pick]
        post_key = post.put()

        user = users.get_current_user()

        # update author entity
        user_account = Author.query( Author.identity == user.user_id()).get()
        # impossible to be none
        # assert user_account == None
        user_account.a_postkey.append(post_key)
        user_account.put()

        # new tag entity or update tag entity
        temp_tag = [x.strip() for x in self.request.get('input_tag').lower().split(",")]
        # store unique tag
        if temp_tag:
            # only first three tag will take
            temp_tag = temp_tag[:3]
            listofentity = []
            for i in temp_tag:
                tags = Tags()
                exist_tag = Tags.query(Tags.tag == i )
                eldertag = exist_tag.get()
                if eldertag is None:
                    tags.tag = i
                    tags.postskey = [post_key]
                    listofentity.append(tags)
                else:
                    eldertag.postskey.append(post_key)
                    listofentity.append(eldertag)
            ndb.put_multi(listofentity)
        self.redirect('/')

app = webapp2.WSGIApplication([
        ('/', MainPage),
        ('/index', IndexPage),
        ('/post', PostPage),
        ('/Image', Imagehandler),
        ('/profile', ProfilePage),
        ('/email',Emailsendhandler),
        ('/login', Loginhandler),
        ('/unsub', Unsubscriptionhandler),
        ('/sub', Subscriptionhandler),
        ('/mobile/home', MobileGethandler),
        ('/mobile/post', MobilePosthandler),
        ('/*', Errorpagehandler),
], debug=True)
