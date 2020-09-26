package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGSign;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.Jar;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.arpg.actor.Mew;
import ch.epfl.cs107.play.game.arpg.actor.Rock;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Grotte extends ARPGArea {

    private Rock rock;

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/Village", new DiscreteCoordinates(25, 17), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(16, 0));
        Door door2 = new Door("GrotteMew", new DiscreteCoordinates(8, 3), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(6, 32));

        for(int i=3;i<=4;++i) {
            for(int j=1;j<=3;++j) {
                rock = new Rock(this, Orientation.DOWN, new DiscreteCoordinates(3+i, 8*j+2));
                registerActor(rock);
                //Calling registerActors method in ARPGArea
            }
        }

        Rock rock = new Rock(this, Orientation.DOWN, new DiscreteCoordinates(5, 26));
        Mew mew = new Mew(this,new DiscreteCoordinates(23,30));
        ARPGSign sign = new ARPGSign("Password : South-West, South-East, North, East, West",this,Orientation.DOWN,new DiscreteCoordinates(23,36));
        Jar jar1 = new Jar(this,Orientation.DOWN,new DiscreteCoordinates(21,37));
        Jar jar2 = new Jar(this,Orientation.DOWN,new DiscreteCoordinates(10,25));
        Jar jar3 = new Jar(this,Orientation.DOWN,new DiscreteCoordinates(10,30));
        Jar jar4 = new Jar(this,Orientation.DOWN,new DiscreteCoordinates(16,25));
        Jar jar5 = new Jar(this,Orientation.DOWN,new DiscreteCoordinates(22,2));

        registerActors(door1, door2,rock,mew,sign,jar1, jar2, jar3,jar4,jar5, new Background(this));
        //Calling registerActors method in ARPGArea

        registerActors(
                new LogMonster(this,new DiscreteCoordinates(3,13)),
                new LogMonster(this,new DiscreteCoordinates(5,15)),
                new LogMonster(this,new DiscreteCoordinates(4,14)),
                new FlameSkull(this,new DiscreteCoordinates(7,18)),
                new FlameSkull(this, new DiscreteCoordinates(8,16)),

                 new LogMonster(this, new DiscreteCoordinates(5,20)),
                 new LogMonster(this, new DiscreteCoordinates(6,20)),
                 new FlameSkull(this, new DiscreteCoordinates(5,19)),
                 new FlameSkull(this, new DiscreteCoordinates(6,19))
        );
    }


    @Override
    public String getTitle() {
        return "Grotte";
    }
}