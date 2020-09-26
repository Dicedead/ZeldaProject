package ch.epfl.cs107.play.game.arpg.actor;

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

public class CaveDoor extends Door {

    protected Logic signal;
    private PressurePlate pressurePlate;

    private final Sprite spriteCaveDoorClosed;
    private final Sprite spriteCaveDoorOpened;

    /**
     * Default CastleDoor constructor
     * @param destination        (String): Name of the destination area, not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     * @param discreteCoordinates (DiscreteCoordinate): Initial position of the entity, not null
     */
    public CaveDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area, Orientation orientation,PressurePlate press, DiscreteCoordinates position, DiscreteCoordinates... discreteCoordinates) {
        super(destination, otherSideCoordinates, Logic.FALSE, area, orientation, position,discreteCoordinates);
        signal = Logic.FALSE;
        spriteCaveDoorClosed = new RPGSprite("zelda/cave.close", 1.f, 1.5f, this,
                new RegionOfInterest(0, 0, 32, 32), new Vector(0.2f,-0.1f), 1.f, -10);
        spriteCaveDoorOpened = new RPGSprite("zelda/cave.open", 1.f, 1.5f, this,
                new RegionOfInterest(0, 0, 32, 32), new Vector(0.2f,-0.1f), 1.f, -10);
        pressurePlate = press;
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

    public boolean getIsOpen() {
        return signal.isOn();
    }

    @Override
    public void draw(Canvas canvas) {
        if(signal == Logic.TRUE) {
            spriteCaveDoorOpened.draw(canvas);
        }
        else {
            spriteCaveDoorClosed.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {

        if (pressurePlate.getSignal() == Logic.TRUE) {
            openDoor();
        }

        super.update(deltaTime);
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