package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Computer;
import ch.epfl.cs107.play.game.arpg.actor.Jar;
import ch.epfl.cs107.play.game.arpg.actor.NPCCenter;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class PetalburgCenter extends ARPGArea {

    private Jar jar;

    @Override
    public String getTitle() {
        return "PetalburgCenter";
    }

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/Village", new DiscreteCoordinates(10, 6), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(6, 0), new DiscreteCoordinates(7, 0));

        NPCCenter doctor = new NPCCenter(this, Orientation.DOWN, new DiscreteCoordinates(7, 6));
        Computer computer = new Computer("zelda/MiniMap", new DiscreteCoordinates(6, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(10, 8));

        jar = new Jar(this, Orientation.DOWN, new DiscreteCoordinates(4, 3));

        registerActors(door1, doctor, computer, jar, new Background(this), new Foreground(this));
        //Calling registerActors method in ARPGArea
    }
}

