package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectableDropperEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.EnvironmentEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class CherryTree extends AreaEntity implements EnvironmentEntity, CollectableDropperEntity {

    private static final int CUT_ANIMATION_DURATION = 3;

    private final Sprite spriteCherryTree = new RPGSprite("zelda/cherryTree", 2.f, 2.f, this,
            new RegionOfInterest(0, 0, 32, 32), Vector.ZERO, 1.f, -10);

    private Animation slicingAnimation;
    private boolean isCut;

    /**
     * Default Cherry Tree constructor, setting the fact that the tree isn't full of cherries
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public CherryTree(Area area, DiscreteCoordinates position) {
        super(area, Orientation.UP, position);

        Sprite[] slicingSprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            slicingSprites[i] = new RPGSprite("zelda/cherryTreeCut", 2, 2, this, new
                    RegionOfInterest(i * 32, 0, 32, 32), Vector.ZERO, 1.f, -10);
        }
        slicingAnimation = new Animation(CUT_ANIMATION_DURATION, slicingSprites, false);
    }

    /**
     * Its main purpose is to update using the method update defined in the superclass
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
            spriteCherryTree.draw(canvas);
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

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    public void setIsCut() {
        isCut = true;
        getOwnerArea().registerActor(new Cherry(getOwnerArea(), getCurrentCells().get(0)));
    }

    @Override
    public Droppables dropItem() {
        return Droppables.CHERRY;
    }
}