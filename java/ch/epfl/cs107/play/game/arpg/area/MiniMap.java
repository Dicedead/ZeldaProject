package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class MiniMap extends ARPGArea {

    @Override
    public String getTitle() {
        return "zelda/MiniMap";
    }

    @Override
    protected void createArea() {

        Door door1 = new Door("zelda/Ferme", new DiscreteCoordinates(6, 8), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(3, 13));
        Door door2 = new Door("zelda/Village", new DiscreteCoordinates(7, 15), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(2, 6));
        Door door3 = new Door("zelda/PontTropLong", new DiscreteCoordinates(9, 1), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(16, 19));
        Door door4 = new Door("zelda/RouteTemple", new DiscreteCoordinates(2, 4), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(21, 14));
        Door door5 = new Door("zelda/Route", new DiscreteCoordinates(9, 7), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(16, 12));
        Door door6 = new Door("zelda/RouteChateau", new DiscreteCoordinates(9, 3), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(16, 30));

        registerActors(door1, door2, door3, door4, door5, door6, new Background(this));
        //Calling registerActors method in ARPGArea
    }

}
