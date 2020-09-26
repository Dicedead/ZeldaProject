package ch.epfl.cs107.play.game.areagame.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class CollectableAreaEntity extends AreaEntity {

    private boolean isCollected;
    private boolean hasBeenCollected;
   // protected Animation anim;

    public CollectableAreaEntity(Area area, DiscreteCoordinates position) {
        super(area, Orientation.UP, position);
        isCollected = false;
        hasBeenCollected = false;
        setAnim();
    }

    abstract protected void setAnim();
    //May return null, in the eventuality that the item is invisible

    abstract protected Animation getCurrentAnimation();

    public boolean isHasBeenCollected() {
        return hasBeenCollected;
    }

    public void setIsCollected() {
        isCollected = true;
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

    /**
     * Primarily updates the collection state of the entity
     *    -hasBeenCollected avoids double additions
     *
     * @param deltaTime (float) not null
     */
    @Override
    public void update(float deltaTime) {
        if (isCollected) {
            hasBeenCollected = true;
            getOwnerArea().unregisterActor(this);
        }
        if (getCurrentAnimation() != null) {
            getCurrentAnimation().update(deltaTime);
        }
    }

}