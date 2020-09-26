package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.EnvironmentEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class PressurePlate extends AreaEntity implements EnvironmentEntity {

    private final Sprite SPRITE_ON;
    private final Sprite SPRITE_OFF;
    private Signal isOn;

    /**
     * Default Pressure plate constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public PressurePlate(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        SPRITE_ON = new RPGSprite("zelda/pressurePlate", 1.f, 1.5f, this,
                new RegionOfInterest(18, 0, 14, 32), Vector.ZERO, 1.f, -10);
        SPRITE_OFF = new RPGSprite("zelda/pressurePlate", 1.f, 1.5f, this,
                new RegionOfInterest(0, 0, 16, 32), Vector.ZERO, 1.f, -10);
        isOn = Logic.FALSE;
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    public void setOn() {
        isOn = Logic.TRUE;
    }

    public Signal getSignal() {
        return isOn;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOn == Logic.TRUE) {
            SPRITE_ON.draw(canvas);
        } else {
            SPRITE_OFF.draw(canvas);
        }
    }

}