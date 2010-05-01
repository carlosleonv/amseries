from google.appengine.ext import webapp
from webtest import TestApp
import main
import unittest

class MainPageTest(unittest.TestCase):

  def setUp(self):
      self.application = webapp.WSGIApplication([('/', main.MainPage)], debug=True)

  def test_default_page(self):
      app = TestApp(self.application)
      response = app.get('/')
      self.assertEqual('200 OK', response.status)
      self.assertTrue('Search series' in response)

class SearchSerieTest(unittest.TestCase):

  def setUp(self):
      self.application = webapp.WSGIApplication([('/', main.SearchSerie)], debug=True)
      
  def test_page_with_param(self):
      app = TestApp(self.application)
      response = app.post('/search', 'name=l')
      self.assertEqual('200 OK', response.status)
      self.assertTrue('Lost' in response)
