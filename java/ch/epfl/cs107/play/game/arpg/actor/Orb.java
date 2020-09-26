package ch.epfl.cs107.play.game.arpg.actor;

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
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Orb extends AreaEntity implements EnvironmentEntity {

    private Animation[] colorsAnimations;
    private Animation currentAnimation;
    private final static int ORB_ANIM_TIME = 4;
    private OrbStates currentState;
    private Bridge bridge;

    private final static int CLOSE_COUNTER_RESET = 160;
    private int closeCounter;

    /**
     * Default Orb constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orbPosition (DiscreteCoordinates): Initial position of the entity in the Area. Not null
     * @param bridgePos   (DiscreteCoordinates): Initial position of the bridge tied to the entity in the Area
     */
    public Orb(Area area, DiscreteCoordinates orbPosition, DiscreteCoordinates bridgePos) {
        super(area, Orientation.UP, orbPosition);

        Sprite[][] colorsSprites = RPGSprite.extractSprites("zelda/orb", 6, 1.f, 1.f, this, 32, 32,
                Orientation.values());
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 6; ++j) {
                colorsSprites[i][j].setAnchor(new Vector(0.13f, 0.4f));
                colorsSprites[i][j].setDepth(-10);
            }
        }
        colorsAnimations = RPGSprite.createAnimations(ORB_ANIM_TIME, colorsSprites, true);
        currentAnimation = colorsAnimations[0];
        currentState = OrbStates.DEFAULT;

        bridge = new Bridge(getOwnerArea(), bridgePos);
        getOwnerArea().registerActor(bridge);

        closeCounter = CLOSE_COUNTER_RESET;
    }

    @Override
    public void update(float deltaTime) {
        switch (currentState) {
            case OPEN_BRIDGE:
                --closeCounter;
                if (closeCounter < CLOSE_COUNTER_RESET / 2) {
                    currentState = OrbStates.CLOSING_BRIDGE;
                }
                break;

            case CLOSING_BRIDGE:
                --closeCounter;
                if (closeCounter <= 0) {
                    currentState = OrbStates.DEFAULT;
                    bridge.closeBridge();
                }
                break;

            case DEFAULT:
                closeCounter = CLOSE_COUNTER_RESET;
                break;
        }

        currentAnimation = colorsAnimations[currentState.ordinal()];
        currentAnimation.update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    public void setOpenBridge() {
        currentState = OrbStates.OPEN_BRIDGE;
        bridge.openBridge();
    }

    public boolean isDefaultState() {
        return currentState == OrbStates.DEFAULT;
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

    private enum OrbStates {
        DEFAULT,
        OPEN_BRIDGE,
        CLOSING_BRIDGE,
        ;
    }

    public class Bridge extends AreaEntity implements EnvironmentEntity, Interactable {

        private final Sprite bridgeSprite;
        private Logic signal;

        /**
         * Default Bridge constructor
         *
         * @param area     (Area): Owner area. Not null
         * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
         */
        public Bridge(Area area, DiscreteCoordinates position) {
            super(area, Orientation.UP, position);
            bridgeSprite = new RPGSprite("zelda/bridge", 3.75f, 2.4f, this, new RegionOfInterest(0, 0,
                    64, 48), new Vector(-0.90f,-0.53f), 1f, -8);
            signal = Logic.FALSE;
        }

       /* @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
        }*/

        @Override
        public void draw(Canvas canvas) {
            if (signal.isOn()) {
                bridgeSprite.draw(canvas);
            }
        }

        private void openBridge() {
            signal = Logic.TRUE;
        }

        private void closeBridge() {
            signal = Logic.FALSE;
        }

        @Override
        public List<DiscreteCoordinates> getCurrentCells() {
            return Collections.singletonList(getCurrentMainCellCoordinates());
        }

        @Override
        public boolean takeCellSpace() {
            return !signal.isOn();
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
        public void acceptInteraction(AreaInteractionVisitor v) {
            ((ARPGInteractionVisitor) v).interactWith(this);
        }
    }
}
