package com.dhf;

import com.dhf.factory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private Ship ship;
    private Captain captain;
    private Sailor sailor;

    public static void main(String[] args) {
        Application app = new Application();

        app.createTeam(new YoungTeamFactory());
        LOGGER.info("正在创建一支年轻的队伍...");
        LOGGER.info("-->" + app.getCaptain().getDescription());
        LOGGER.info("-->" + app.getShip().getDescription());
        LOGGER.info("-->" + app.getSailor().getDescription());

        app.createTeam(new PermanentTeamFactory());
        LOGGER.info("正在创建一支久经考验的队伍...");
        LOGGER.info("-->" + app.getCaptain().getDescription());
        LOGGER.info("-->" + app.getShip().getDescription());
        LOGGER.info("-->" + app.getSailor().getDescription());
    }

    public void createTeam(final TeamFactory factory) {
        setCaptain(factory.createCaptain());
        setShip(factory.createShip());
        setSailor(factory.createSailor());
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Captain getCaptain() {
        return captain;
    }

    public void setCaptain(Captain captain) {
        this.captain = captain;
    }

    public Sailor getSailor() {
        return sailor;
    }

    public void setSailor(Sailor sailor) {
        this.sailor = sailor;
    }
}
