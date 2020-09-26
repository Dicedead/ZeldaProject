package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.King;
import ch.epfl.cs107.play.game.arpg.actor.Torch;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Chateau extends ARPGArea {
    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/RouteChateau", new DiscreteCoordinates(9, 12), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(7, 0), new DiscreteCoordinates(8, 0));
        King king = new King(this, Orientation.DOWN, new DiscreteCoordinates(7, 12));
        Torch torch1 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(5, 13));
        Torch torch2 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(10, 13));
        Torch torch3 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(4, 10));
        Torch torch4 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(11, 10));
        Torch torch5 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(11, 6));
        Torch torch6 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(4, 6));

        registerActors(door1, king, torch1, torch2, torch3, torch4, torch5, torch6, new Background(this), new Foreground(this));
        //Calling registerActors method in ARPGArea
    }


    @Override
    public String getTitle() {
        return "zelda/Chateau";
    }

}
