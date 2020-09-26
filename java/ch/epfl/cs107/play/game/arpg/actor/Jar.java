package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

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
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Jar extends AreaEntity implements CollectableDropperEntity {


    private static final int DESTROYED_ANIMATION_DURATION = 3;

    private final Sprite spriteJar = new RPGSprite("zelda/jar", 1.f, 2.f, this,
            new RegionOfInterest(0, 0, 16, 32), Vector.ZERO, 1.f, -10);

    private Animation implosionAnimation;
    private boolean isDestroyed;
    private boolean hasDropped;

    public Jar(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        isDestroyed = false;
        hasDropped = false;
        Sprite[] implosionSprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            implosionSprites[i] = new RPGSprite("zelda/jar", 1, 1, this, new
                    RegionOfInterest(i * 16, 0, 16, 32), Vector.ZERO, 1.f, -10);
        }
        implosionAnimation = new Animation(DESTROYED_ANIMATION_DURATION, implosionSprites, false);
    }

    /**
     * Its main purpose is to update the destruction animation
     *
     * @param deltaTime (float)
     */
    @Override
    public void update(float deltaTime) {

        if (isDestroyed) {
            implosionAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {

        if (!isDestroyed) {
            spriteJar.draw(canvas);
        } else {
            endingAnimationAndUnregister(implosionAnimation, canvas, getOwnerArea(), this);
        }

    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return !isDestroyed;
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
    public void setIsDestroyed() {
        isDestroyed = true;
        if (!hasDropped) {
            Droppables droppedItem = dropItem();
            switch (droppedItem) {
                case COIN:
                    getOwnerArea().registerActor(new Coin(getOwnerArea(), getCurrentCells().get(0)));
                    break;
                case HEART:
                    getOwnerArea().registerActor(new Heart(getOwnerArea(), getCurrentCells().get(0)));
                    break;
                default:
                    break;
            }
            hasDropped = true;
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public Droppables dropItem() {
        double randomGen = RandomGenerator.getInstance().nextDouble();
        if (randomGen < 0.3) {
            return Droppables.HEART;
        } else{
            return Droppables.COIN;
        }
    }
}