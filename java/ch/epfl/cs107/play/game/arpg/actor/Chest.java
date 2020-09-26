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
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Chest extends AreaEntity implements CollectableDropperEntity {


    private static final int OPENING_ANIMATION_DURATION = 3 ;

    private final Sprite spriteChestClosed = new RPGSprite("zelda/chest.opening", 1.f, 2.f, this,
            new RegionOfInterest(0, 0, 16, 32), Vector.ZERO, 1.f, -10);

    private Logic signal;
    private Animation openingAnimation;
    private String chestCode;
    private boolean isOpened;

    public Chest(Area area, Orientation orientation, DiscreteCoordinates position,String code) {
        super(area, orientation, position);
        signal =Logic.FALSE;
        chestCode = code;
        isOpened = false;

        Sprite[] openingSprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            openingSprites[i] = new RPGSprite("zelda/chest.opening", 1, 2, this, new
                    RegionOfInterest(16*i, 0, 16, 32), Vector.ZERO, 1.f, -10);
        }
        openingAnimation = new Animation(OPENING_ANIMATION_DURATION, openingSprites, false);
    }

    /**
     * Its main purpose is to update the slicing animation
     *
     * @param deltaTime (float)
     */
    @Override
    public void update(float deltaTime) {

        if (isOpened) {
            openingAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {

        if (!isOpened) {
            spriteChestClosed.draw(canvas);
        } else {
            openingAnimation.draw(canvas);
        }

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
        return signal == Logic.FALSE;
    }

    public String getChestCode() {
        return chestCode;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    public void setIsOpenable() {
        signal = Logic.TRUE;
    }

    public boolean getIsOpenable() {
        return signal == Logic.TRUE;
    }

    public boolean openChest() {
        isOpened = true;
        return isOpened;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

}
