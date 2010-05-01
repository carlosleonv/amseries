package com.badbuxtour.test.controller;

import org.junit.Test;

import com.badbuxtour.dao.Player;

public class PlayerTestCase extends BaseTestCase {

    private void insertEmptyPlayer() {
	Player p = new Player();
	try {
	    playerController.create(p);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    @Test
    public void testCountPlayer() {
	int count = playerController.count();
	assertEquals("Test testCountPlayer error", 0, count);
	insertEmptyPlayer();
	count = playerController.count();
	assertEquals("Test testCountPlayer error", 1, count);
    }

}
