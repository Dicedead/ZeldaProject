package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectableDropperEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Grass extends AreaEntity implements CollectableDropperEntity {

    private static final int CUT_ANIMATION_DURATION = 3;

    private final Sprite spriteGrass = new RPGSprite("zelda/grass", 1.f, 1.f, this,
            new RegionOfInterest(0, 0, 16, 16), Vector.ZERO, 1.f, -10);

    private Animation slicingAnimation;
    private boolean isCut;
    private boolean hasDropped;

    /**
     * Default Grass constructor, including its default sprite and slicing animation
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Grass(Area area, DiscreteCoordinates position) {
        super(area, Orientation.UP, position);
        isCut = false;
        hasDropped = false;
        Sprite[] slicingSprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            slicingSprites[i] = new RPGSprite("zelda/grass.sliced", 1, 1, this, new
                    RegionOfInterest(i * 32, 0, 32, 32), Vector.ZERO, 1.f, -10);
        }
        slicingAnimation = new Animation(CUT_ANIMATION_DURATION, slicingSprites, false);

    }

    /**
     * Its main purpose is to update the slicing animation
     *
     * @param deltaTime (float)
     */
    @Override
    public void update(float deltaTime) {

        if (isCut) {
            slicingAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {

        if (!isCut) {
            spriteGrass.draw(canvas);
        } else {
            endingAnimationAndUnregister(slicingAnimation,canvas,getOwnerArea(),this);
        }

    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return !isCut;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     *
     */
    public void setIsCut() {
        isCut = true;
        if (!hasDropped) {
            Droppables droppedItem = dropItem();
            switch (droppedItem) {
                case COIN:
                    getOwnerArea().registerActor(new Coin(getOwnerArea(), getCurrentCells().get(0)));
                    break;
                case HEART:
                    getOwnerArea().registerActor(new Heart(getOwnerArea(), getCurrentCells().get(0)));
                    break;
            }
            hasDropped = true;
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
