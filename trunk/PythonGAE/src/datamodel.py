from google.appengine.api import urlfetch, images, users
from google.appengine.ext import db

#############################
# DATA MODEL
#############################
class Genre(db.Model):
    name = db.StringProperty()

class Serie(db.Model):
    title = db.StringProperty()
    desc = db.StringProperty()
    timestamp = db.DateTimeProperty(auto_now_add=True)
    genre = db.ReferenceProperty(Genre)
    picture = db.BlobProperty(default=None)
    thumbnail_pict = db.BlobProperty(default=None)

class User_has_series(db.Model):
    owner = db.UserProperty(required=True)
    serie = db.ReferenceProperty(Serie)
    episodes = db.StringProperty()

class Site(db.Model):
    url = db.StringProperty()

class Episode(db.Model):
    owner = db.UserProperty(required=True)
    serie = db.ReferenceProperty(Serie, required=True)
    episodeNb = db.IntegerProperty(int)
    episodeName = db.StringProperty()
    urls = db.StringListProperty()
    
#############################
def populate_db():
    # Create genre
    genre = Genre(name = 'American')
    genre.put()
    # Create series
    #s1 = create_serie('Lost', 'ils sont perdus', genre.key(), urlfetch.Fetch('http://www.blogtendances.com/wp-content/uploads/2008/01/320px-lost-season2.jpg').content)    
    #s2 = create_serie('Heroes', 'ce sont des heros', genre.key(), urlfetch.Fetch('http://www.qctop.com/articles/upload/heroes-46449.jpg').content)  
    #s3 = create_serie('Desperate housewives', 'Desperate housewives', genre.key(), urlfetch.Fetch('http://www.trimtab.fr/wp-content/uploads/2009/01/desperate-housewives.jpg').content)
    s1 = create_serie('Lost', 'ils sont perdus', genre.key(), None)    
    s2 = create_serie('Heroes', 'ce sont des heros', genre.key(), None)  
    s3 = create_serie('Desperate housewives', 'Desperate housewives', genre.key(), None) 
    User_has_series(owner = users.get_current_user(),
                    serie = s1
                    ).put()
    Episode(owner = users.get_current_user(),
                serie = s1,
                episodeNb = 1,
                episodeName = 'episode 1',
                urls = ['http://www.test.com', 'http://www.test.com', 'http://www.test.com']).put()
    #serie1 = Serie(title = 'Lost', desc = 'ils sont perdus', genre = genre.key())
    #serie1.picture = db.Blob(urlfetch.Fetch('http://www.blogtendances.com/wp-content/uploads/2008/01/320px-lost-season2.jpg').content)
    #serie1.put()
    #serie2 = Serie(title = 'Heroes', desc = 'ce sont des heros', genre = genre.key())
    #serie2.picture = db.Blob(urlfetch.Fetch('http://www.qctop.com/articles/upload/heroes-46449.jpg').content)
    #serie2.put()
    
#############################
# Generic persistence methods
#############################
def find(key_name):
    return db.get(db.Key(key_name))
    
def drop(key_name):
    return db.delete(db.Key(key_name))

#############################
# GENRES p m
#############################
def find_all_genres():
    q = Genre.all()
    return q.fetch(10)

#############################
# SERIES p m
#############################
def create_serie(title, desc, genre_key, img_data):
    if img_data == None:
        png_data = None
        thumbnail_data = None
    else:
        img = images.Image(img_data)
        img.im_feeling_lucky()
        png_data = img.execute_transforms(images.PNG)
        img.resize(60, 100)
        thumbnail_data = img.execute_transforms(images.PNG)
        
    s = Serie(title=title,
          genre=genre_key,
          desc=desc,
          picture=png_data,
          thumbnail_pict=thumbnail_data).put()
    return s

def search_series(name):
    q = Serie.all()#.filter('title = ', name)
    series = q.fetch(10)
    found_series = []
    for s in series:
        str = s.title.upper()
        if str.startswith(name.upper()):
            found_series.append(s)
    return found_series

def find_serie(name):
    q = Serie.all().filter('title = ', name)
    return q.get()
    
def find_serie_4_user(user):
    q = User_has_series.all().filter('owner = ', user)
    uhs_list = q.fetch(10)
    return uhs_list

def add_serie_4_user(user, key_name):
    q = User_has_series.all().filter('owner = ', user).filter('serie = ', db.Key(key_name))
    results = q.fetch(10)
    if len(results) == 0:
        uhs = User_has_series(owner = user, serie = db.Key(key_name))
        uhs.put()
        return 1
    else:
        return 0

def edit_serie(key_name, episodes):
    uhs = find(key_name)
    uhs.episodes = episodes
    uhs.put()

def remove_serie(key_name):
    uhs = find(key_name)
    if uhs.serie.episode_set.count() > 0:
        db.delete(uhs.serie.episode_set)
    db.delete(uhs)

# drop serie (admin only)
def drop_serie(key_name):
    drop(key_name)
    q = User_has_series.all().filter('serie = ', db.Key(key_name))
    results = q.fetch(10)
    db.delete(results)

#############################
# EPISODES p m
#############################
def find_episodes_4_user(user):
    q = Episode.all().filter('owner = ', user)
    return q.fetch(10)

#############################
# SITES p m
#############################
def find_all_sites():
    q = Site.all()
    return q.fetch(10)

def add_site(url):
    site = Site(url = url)
    site.put()