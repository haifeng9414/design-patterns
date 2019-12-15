package com.dhf.factory;

public class PermanentTeamFactory implements TeamFactory {

    public Ship createShip() {
        return new OldShip();
    }

    public Captain createCaptain() {
        return new OldCaptain();
    }

    public Sailor createSailor() {
        return new OldSailor();
    }
}
