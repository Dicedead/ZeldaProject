package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.Coin;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.Heart;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.Orb;
import ch.epfl.cs107.play.game.arpg.actor.Waterfall;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Route extends ARPGArea {

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/Ferme", new DiscreteCoordinates(18,15), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(0,15), new DiscreteCoordinates(0,16));
        Door door2 = new Door("zelda/Village", new DiscreteCoordinates(29,18), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(9,0), new DiscreteCoordinates(10,0));
        Door door3 = new Door("zelda/PontTropLong", new DiscreteCoordinates(9,1), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(9,19), new DiscreteCoordinates(10,19));
        Door door4 = new Door("zelda/RouteTemple", new DiscreteCoordinates(1,4), Logic.TRUE, this, Orientation.RIGHT,
                new DiscreteCoordinates(19,9), new DiscreteCoordinates(19,10), new DiscreteCoordinates(19,11));


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 6; ++j) {
                registerActor(new Grass(this, new DiscreteCoordinates(i+5,j+6)));
                //Calling registerActors method in ARPGArea
            }
        }

        Waterfall waterfall = new Waterfall(this,Orientation.DOWN, new DiscreteCoordinates(15,3));
        Orb orb = new Orb(this,new DiscreteCoordinates(19,8),new DiscreteCoordinates(16,10));

        Bomb boom = new Bomb(this, new DiscreteCoordinates(6,11),true);
        FlameSkull flamey = new FlameSkull(this, new DiscreteCoordinates(6,7));
        LogMonster loggy = new LogMonster(this, new DiscreteCoordinates(6,14));

        Coin coin = new Coin(this, new DiscreteCoordinates(6,13));
        Heart heart = new Heart(this, new DiscreteCoordinates(8,13));

        registerActors(door1, door2, door3, door4, new Background(this), new Foreground(this), waterfall, orb,
                boom, flamey, loggy, coin,heart);
        //Calling registerActors method in ARPGArea
    }

    @Override
    public String getTitle() {
        return "zelda/Route";
    }
}
