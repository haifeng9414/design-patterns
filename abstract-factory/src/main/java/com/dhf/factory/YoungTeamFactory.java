package com.dhf.factory;

public class YoungTeamFactory implements TeamFactory {

    public Ship createShip() {
        return new NewShip();
    }

    public Captain createCaptain() {
        return new YoungCaptain();
    }

    public Sailor createSailor() {
        return new YoungSailor();
    }
}
