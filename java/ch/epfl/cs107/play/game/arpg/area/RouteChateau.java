package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class RouteChateau extends ARPGArea {

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/PontTropLong", new DiscreteCoordinates(9,18), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0));

        CastleDoor castleDoor = new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7,1), this, Orientation.UP, new DiscreteCoordinates(9,13),
                new DiscreteCoordinates (10,13));
        DarkLord darky = new DarkLord(this, new DiscreteCoordinates(8,8));

        CastleKey castleKey = new CastleKey(this, new DiscreteCoordinates(8,5));

        registerActors(door1,castleDoor,darky, new Background(this), new Foreground(this));
    }

    @Override
    public String getTitle() {
        return "zelda/RouteChateau";
    }
}
