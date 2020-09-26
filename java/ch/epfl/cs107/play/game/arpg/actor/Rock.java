package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
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

public class Rock extends AreaEntity implements EnvironmentEntity, Interactable {


    private static final int IMPLOSION_ANIMATION_DURATION = 3;

    private final Sprite spriteRock = new RPGSprite("rock.3", 1.f, 1.f, this,
            new RegionOfInterest(0, 0, 16, 16), Vector.ZERO, 1.f, -10);
    private Animation implodingAnimation;
    private boolean isImploded;

    /**
     * Default Rock constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        isImploded = false;
        Sprite[] implodedSprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            implodedSprites[3-i] = new RPGSprite("zelda/rock.imploded", 1, 1, this, new
                    RegionOfInterest(i * 32, 0, 32, 32), Vector.ZERO, 1.f, -10);
        }
        implodingAnimation = new Animation(IMPLOSION_ANIMATION_DURATION, implodedSprites, false);
    }

    @Override
    public void update(float deltaTime) {
        if (isImploded) {
            implodingAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isImploded) {
            spriteRock.draw(canvas);
        } else {
            endingAnimationAndUnregister(implodingAnimation,canvas,getOwnerArea(),this);
        }

    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {

        return !isImploded;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    public void setIsImploded() {
        isImploded = true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}

