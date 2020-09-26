package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Arrow;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Pont extends ARPGArea {

    private int count = 14;
    private final static int MODULO_LEFT_ARROW = 37;
    private final static int MODULO_RIGHT_ARROW = 23;


    @Override
    public String getTitle() {
        return "zelda/PontTropLong";
    }

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/RouteChateau", new DiscreteCoordinates(9, 1), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(9, 19), new DiscreteCoordinates(10, 19));

        Door door2 = new Door("zelda/Route", new DiscreteCoordinates(9, 18), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(9, 0), new DiscreteCoordinates(10, 0));

        registerActors(door1, door2, new Background(this));
        //Calling registerActors method in ARPGArea
    }

    @Override
    public void update(float deltaTime) {
        if (count % MODULO_RIGHT_ARROW == 0) {
            Arrow arrow1 = new Arrow(this, Orientation.DOWN, new DiscreteCoordinates(10, 19));
            Arrow arrow2 = new Arrow(this, Orientation.LEFT, new DiscreteCoordinates(19, 13));
            registerActors(arrow1, arrow2);
            //Calling registerActors method in ARPGArea
        }
        if (count % MODULO_LEFT_ARROW == 0) {
            Arrow arrow1 = new Arrow(this, Orientation.DOWN, new DiscreteCoordinates(9, 19));
            Arrow arrow2 = new Arrow(this, Orientation.LEFT, new DiscreteCoordinates(19, 7));
            registerActors(arrow1, arrow2);
            //Calling registerActors method in ARPGArea
        }

        super.update(deltaTime);
        ++count;
    }
}
