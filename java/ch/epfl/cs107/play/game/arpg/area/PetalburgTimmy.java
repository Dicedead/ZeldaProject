package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Assistant;
import ch.epfl.cs107.play.game.arpg.actor.Computer;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class PetalburgTimmy extends ARPGArea {

    @Override
    public String getTitle() {
        return "PetalburgTimmy";
    }

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/Ferme", new DiscreteCoordinates(6, 10), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(4, 0), new DiscreteCoordinates(3, 0));
        Computer computer = new Computer("zelda/MiniMap", new DiscreteCoordinates(3,11),Logic.TRUE,this, Orientation.UP, new DiscreteCoordinates(2,7));

        Assistant mom = new Assistant(this,Orientation.DOWN, new DiscreteCoordinates(8,5));
        registerActors(door1, computer,  mom, new Background(this), new Foreground(this));
        //Calling registerActors method in ARPGArea
    }
}

