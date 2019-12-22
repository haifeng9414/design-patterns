package com.dhf;

import com.dhf.template.Cricket;
import com.dhf.template.Football;
import com.dhf.template.Game;

public class Application {
    public static void main(String[] args) {
        Game game = new Cricket();
        game.play();
        System.out.println();
        game = new Football();
        game.play();
    }
}
