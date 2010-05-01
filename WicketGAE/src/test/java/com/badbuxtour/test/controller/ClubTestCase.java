package com.badbuxtour.test.controller;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.badbuxtour.dao.Club;
import com.badbuxtour.dao.Player;

public class ClubTestCase extends BaseTestCase {

    private void insertEmptyClub() {
	Club c = new Club();
	try {
	    clubController.create(c);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testCountClub() {
	int count = clubController.count();
	assertEquals("Test testCountClub error", 0, count);
	insertEmptyClub();
	count = clubController.count();
	assertEquals("Test testCountClub error", 1, count);
    }

}
