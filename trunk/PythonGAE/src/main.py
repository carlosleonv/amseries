from google.appengine.api import users
from google.appengine.ext import db, webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app
import cgi
import datamodel
import os
from django.utils import simplejson

pathSeries = os.path.join(os.path.dirname(__file__), 'series.html')
pathMySeries = os.path.join(os.path.dirname(__file__), 'mySeries.html')
pathMyDL = os.path.join(os.path.dirname(__file__), 'myDL.html')
pathEditSerie = os.path.join(os.path.dirname(__file__), 'editSerie.html')
pathAdmin = os.path.join(os.path.dirname(__file__), 'admin.html')
pathSites = os.path.join(os.path.dirname(__file__), 'sites.html')
pathCreateSerie = os.path.join(os.path.dirname(__file__), 'createSerie.html')

class BaseRequestHandler(webapp.RequestHandler):
    def generate(self, template_name, template_values={}):
        if users.get_current_user():
            url = users.create_logout_url("/")
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
            
        values = {
                'url': url,
                'url_linktext': url_linktext,
                }
        if users.is_current_user_admin():
            values['admin'] = 'yes'
        
        values.update(template_values)
        directory = os.path.dirname(__file__)
        path = os.path.join(directory, 'templates', template_name)
        self.response.out.write(template.render(path, values))
    
    def reject(self):
        self.response.set_status(404)
        self.response.out.write('Page not found')

class GetImage(webapp.RequestHandler):
    def get(self, display_type, key):
        serie = datamodel.find(key)
        if (serie):
            if (display_type == 'image' and serie.picture):
                self.response.headers['Content-Type'] = 'image/png'
                self.response.out.write(serie.picture)
            elif (display_type == 'thumbnail' and serie.thumbnail_pict):
                self.response.headers['Content-Type'] = 'image/png'
                self.response.out.write(serie.thumbnail_pict)
        else:
            self.redirect('/static/noimage.jpg')
            
class MainPage(BaseRequestHandler):
    def get(self):
        self.generate('series.html', {});
        #self.response.out.write(template.render(pathSeries, getTemplateParams({},self)))

class SearchSerie(BaseRequestHandler):
    def get(self):
        self.generate('series.html', {});
    def post(self):
        name = self.request.get('name')
        template_values = {'series': datamodel.search_series(name)}
        self.generate('series.html', template_values);
        
class AddSerie(BaseRequestHandler):
    def get(self):
        if not users.get_current_user():
            self.redirect(users.create_login_url(self.request.uri))
        else:
            key = self.request.get('key')
            datamodel.add_serie_4_user(users.get_current_user(), key)
            self.redirect('/mySeries')
          
class RemoveSerie(BaseRequestHandler):
    def get(self):
        key = self.request.get('key')
        datamodel.remove_serie(key)
        self.redirect('/mySeries')
        
class EditSerie(BaseRequestHandler):
    def get(self):
        key = self.request.get('key')
        template_values = {'serie': datamodel.find(key)}
        self.generate('editSerie.html', template_values);
    def post(self):
        key = self.request.get('key')
        episodes = self.request.get('episodes')
        datamodel.edit_serie(key, episodes)
        self.redirect('/mySeries')
        
class MySeries(BaseRequestHandler):
    def get(self):
        if not users.get_current_user():
            self.redirect(users.create_login_url(self.request.uri))
        template_values = {'series': datamodel.find_serie_4_user(users.get_current_user())}
        self.generate('mySeries.html', template_values);

class MyDL(BaseRequestHandler):
    def get(self):
        if not users.get_current_user():
            self.redirect(users.create_login_url(self.request.uri))
        template_values = {'series': datamodel.find_serie_4_user(users.get_current_user())}
        self.generate('myDL.html', template_values);
        
#############################
# ADMIN
#############################
class AdminArea(BaseRequestHandler):
    def get(self):
        if users.is_current_user_admin():
            self.generate('admin.html', {});
        else:
            self.reject()
        
class Sites(BaseRequestHandler):
    def get(self):
        if users.is_current_user_admin():
            template_values = {'sites': datamodel.find_all_sites()}
            self.generate('sites.html', template_values);
        else:
            self.reject()
        
class AddSite(BaseRequestHandler):
    def post(self):
        if users.is_current_user_admin():
            url = self.request.get('url')
            if url != "":
                datamodel.add_site(url)
            self.redirect('/sites')
        else:
            self.reject()
        
class DropSite(BaseRequestHandler):
    def get(self):
        if users.is_current_user_admin():
            key = self.request.get('key')
            datamodel.drop(key)
            self.redirect('/sites')
        else:
            self.reject()
        
class DropSerie(BaseRequestHandler):
    def get(self):
        if users.is_current_user_admin():
            key = self.request.get('key')
            datamodel.drop_serie(key)
            self.redirect('/search')
        else:
            self.reject()
        
class CreateSerie(BaseRequestHandler):
    def get(self):
        if users.is_current_user_admin():
            template_values = {'genres': datamodel.find_all_genres()}
            self.generate('createSerie.html', template_values);
        else:
            self.reject()
    def post(self):
        if users.is_current_user_admin():
            title = cgi.escape(self.request.get('title'))
            desc = cgi.escape(self.request.get('desc'))
            genre_key = cgi.escape(self.request.get('genre'))
            img_data = self.request.POST.get('picture')
            if img_data != '':
                img = img_data.file.read()
            else:
                img = None
            datamodel.create_serie(title, desc, db.Key(genre_key), img)
            self.redirect('/')
        else:
            self.reject()
        
class PopulateDB(BaseRequestHandler):
    def get(self):
        datamodel.populate_db()
        self.redirect('/')
        
class RemoveEpisode(BaseRequestHandler):
    def get(self, key):
        datamodel.drop(key)
        self.redirect('/myDL')

class RPCHandler(webapp.RequestHandler):
    """ Allows the functions defined in the RPCMethods class to be RPCed."""
    def __init__(self):
        webapp.RequestHandler.__init__(self)
        self.methods = RPCMethods()

    def post(self):
        args = simplejson.loads(self.request.body)
        func, args = args[0], args[1:]

        if func[0] == '_':
            self.error(403) # access denied
            return

        func = getattr(self.methods, func, None)
        if not func:
            self.error(404) # file not found
            return

        result = func(*args)
        self.response.out.write(simplejson.dumps(result))

class RPCMethods:
    """ Defines the methods that can be RPCed.
    NOTE: Do not allow remote callers access to private/protected "_*" methods.
    """

    def ajaxAddSerie(self, *args):
        keys = args[0]
        if users.get_current_user():
            nb = 0
            for key in keys:
                result = datamodel.add_serie_4_user(users.get_current_user(), key)
                if result == 1:
                    nb = nb + 1
            return nb
        else: 
            return -1
        
    def ajaxRemoveSerie(self, *args):
        keys = args[0]
        if users.get_current_user():
            for key in keys:
                datamodel.remove_serie(key)
        
    def ajaxEditEpisodes(self, *args):
        keys = args[0]
        episodes = args[1]
        if users.get_current_user():
            for key in keys:
                datamodel.edit_serie(key, episodes)
                
    def ajaxDropSerie(self, *args):
        keys = args[0]
        if users.is_current_user_admin():
            for key in keys:
                datamodel.drop_serie(key)
                
    def ajaxViewSerie(self, *args):
        key = args[0]
        o = datamodel.find(key)
        return o.title
        
application = webapp.WSGIApplication(
                                     [('/', MainPage),
                                      ('/search', SearchSerie),
                                      ('/mySeries', MySeries),
                                      ('/populateDB', PopulateDB),
                                      ('/addSerie', AddSerie),
                                      ('/removeSerie', RemoveSerie),
                                      ('/editSerie', EditSerie),
                                      ('/admin', AdminArea),
                                      ('/sites', Sites),
                                      ('/addSite', AddSite),
                                      ('/dropSite', DropSite),
                                      ('/dropSerie', DropSerie),
                                      ('/(thumbnail|image)/([-\w]+)', GetImage),
                                      ('/createSerie', CreateSerie),
                                      ('/myDL', MyDL),
                                      ('/removeEpisode/([-\w]+)', RemoveEpisode),
                                      ('/rpc', RPCHandler)],
                                     debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()
