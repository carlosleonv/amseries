package com.badbuxtour.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badbuxtour.dao.Town;

public class TownJpaController extends GenericJpaController<Town> {

    private Logger logger = LoggerFactory.getLogger(TournamentJpaController.class);

    public TownJpaController() {
	super(Town.class);
    }
}
