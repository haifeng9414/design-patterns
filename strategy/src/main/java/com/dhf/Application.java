package com.dhf;

import com.dhf.strategy.BusinessMan;
import com.dhf.strategy.TransportationAirplane;
import com.dhf.strategy.TransportationTrain;
import com.dhf.strategy.TransportationVehicle;

public class Application {
    public static void main(String[] args) {
        BusinessMan man = new BusinessMan(new TransportationAirplane());
        man.transport();

        man.changeStrategy(new TransportationTrain());
        man.transport();

        man.changeStrategy(new TransportationVehicle());
        man.transport();
    }
}
