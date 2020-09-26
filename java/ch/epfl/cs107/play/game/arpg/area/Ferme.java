package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Bicycle;
import ch.epfl.cs107.play.game.arpg.actor.CherryTree;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Ferme extends ARPGArea {

    protected void createArea() {

        Door door1 = new Door("zelda/Route", new DiscreteCoordinates(1, 15), Logic.TRUE, this, Orientation.RIGHT,
                new DiscreteCoordinates(19, 15), new DiscreteCoordinates(19, 16));
        Door door2 = new Door("zelda/Village", new DiscreteCoordinates(4, 18), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 0));
        Door door3 = new Door("zelda/Village", new DiscreteCoordinates(14, 18), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(13, 0), new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0));
        Door door4 = new Door("PetalburgTimmy", new DiscreteCoordinates(4, 1), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(6, 11));


        CherryTree cherryTree1 = new CherryTree(this, new DiscreteCoordinates(17, 15));
        CherryTree cherryTree2 = new CherryTree(this, new DiscreteCoordinates(17, 16));

        Bicycle bicycle = new Bicycle(this, new DiscreteCoordinates(5, 10));

        registerActors(door1, door2, door3, door4, bicycle, cherryTree1, cherryTree2,
                new Background(this), new Foreground(this));
        //Calling registerActors method in ARPGArea
    }

    public String getTitle() {
        return "zelda/Ferme";
    }
}
