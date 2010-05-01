from google.appengine.ext import db
import datamodel
import logging
import unittest

class SuccessFailError(unittest.TestCase):

    def setUp(self):
        logging.info('In setUp()')
        
    def tearDown(self):
        logging.info('In tearDown()')

    def test_success(self):
        logging.info('Running test_success()')
        self.assertTrue(True)

class ModelTest(unittest.TestCase):

  def setUp(self):
    # Populate test entities.
    entity = datamodel.Serie(title='Bar')
    self.setup_key = entity.put()

  def tearDown(self):
    # There is no need to delete test entities.
    pass

  def test_new_entity(self):
    entity = datamodel.Serie(title='Foo')
    self.assertEqual('Foo', entity.title)

  def test_saved_enitity(self):
    entity = datamodel.Serie(title='Foo')
    key = entity.put()
    self.assertEqual('Foo', db.get(key).title)

  def test_setup_entity(self):
    entity = db.get(self.setup_key)
    self.assertEqual('Bar', entity.title)


