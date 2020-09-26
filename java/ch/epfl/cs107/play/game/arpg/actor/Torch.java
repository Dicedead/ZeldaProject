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

public class Torch extends AreaEntity implements EnvironmentEntity {

    private final Sprite spriteTorchOn;
    private final Sprite spriteTorchOff;
    private Signal isOn;
    private boolean hasBeenUsed;

    public Torch(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        spriteTorchOn =new RPGSprite("zelda/Torch", 1.f, 1.5f, this,
                new RegionOfInterest(16, 0, 16, 32), new Vector(0,0.25f), 1.f, -10);
        spriteTorchOff = new RPGSprite("zelda/Torch", 1.f, 1.5f, this,
                new RegionOfInterest(0, 0, 16, 32), Vector.ZERO, 1.f, -10);
        isOn = Logic.FALSE;
        hasBeenUsed = false;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    public void setOn() {
        isOn = Logic.TRUE;
    }

    public void setOff() {
        isOn = Logic.FALSE;
    }

    public Signal getSignal() {
        return isOn;
    }

    public boolean getUsage() {
        return hasBeenUsed;
    }

    public void resetUsage() {
        hasBeenUsed = false;
    }

    public void setUsage() {
        hasBeenUsed = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOn == Logic.TRUE) {
            spriteTorchOn.draw(canvas);
        } else {
            spriteTorchOff.draw(canvas);
        }

    }

}
