package ch.epfl.cs107.play.game.areagame.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.rpg.actor.FlyableEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Projectile extends MovableAreaEntity implements Interactor, FlyableEntity {


    private final static int PACE_OF_MOVEMENT = 2;

    private final static int MAX_DISTANCE = 16;

    private DiscreteCoordinates initialCoords;
    private boolean isFlying;

    /**
     * Default Projectile constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position        (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        initialCoords = position;
        isFlying = true;
    }

    protected abstract Graphics getRepresentation();

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if(isFlying) {
            getRepresentation().draw(canvas);
        }
        else {
            getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    public List<DiscreteCoordinates> getNextCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    protected void destroyProjectile() {
        isFlying = false;
    }

    public void update(float deltaTime) {
        move(PACE_OF_MOVEMENT);
        if(DiscreteCoordinates.distanceBetween(initialCoords,getCurrentCells().get(0)) > MAX_DISTANCE || !isFlying ||
                !getOwnerArea().canEnterAreaCells(this,getNextCells())) {
            destroyProjectile();
        }

        if (getRepresentation() instanceof Animation) {
            ((Animation)getRepresentation()).update(deltaTime);
        }

        super.update(deltaTime);
    }

}
