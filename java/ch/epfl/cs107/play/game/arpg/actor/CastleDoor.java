package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {

    private Logic signal;

    private final Sprite spriteCastleDoorClosed;
    private final Sprite spriteCastleDoorOpened;

    /**
     * Default CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     * @param discreteCoordinates (DiscreteCoordinate): Initial position of the entity, not null
     */
    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... discreteCoordinates) {
        super(destination, otherSideCoordinates, Logic.FALSE, area, orientation, position,discreteCoordinates);
        signal = Logic.FALSE;
        spriteCastleDoorClosed = new RPGSprite("zelda/castleDoor.close", 2.f, 2f, this,
                new RegionOfInterest(0, 0, 32, 32), Vector.ZERO, 1.f, -10);
        spriteCastleDoorOpened = new RPGSprite("zelda/castleDoor.open", 2.f, 2.f, this,
                new RegionOfInterest(0, 0, 32, 32), Vector.ZERO, 1.f, -10);
    }

    public boolean isOpen() {
        return signal.isOn();
    }

    /**
     * Set door close
     */
    public void closeDoor() {
        signal = Logic.FALSE;
    }

    public void openDoor() {
        signal = Logic.TRUE;
    }

    @Override
    public void draw(Canvas canvas) {
        if(signal == Logic.TRUE) {
            spriteCastleDoorOpened.draw(canvas);
        }
        else {
            spriteCastleDoorClosed.draw(canvas);
        }
    }

    @Override
    public boolean isCellInteractable() {
        return signal == Logic.TRUE;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    public boolean takeCellSpace() {
        return signal != Logic.TRUE;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor)v).interactWith(this);
    }

}