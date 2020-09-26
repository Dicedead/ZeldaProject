package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Staff;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Temple extends ARPGArea {

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/RouteTemple", new DiscreteCoordinates(5,4), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(4,0));

        Staff staff = new Staff(this, new DiscreteCoordinates(4,3));

        registerActors(door1, staff, new Foreground(this), new Background(this));

    }

    @Override
    public String getTitle() {
        return "zelda/Temple";
    }
}
