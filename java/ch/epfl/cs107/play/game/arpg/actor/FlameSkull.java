package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGAggressiveVisitor;
import ch.epfl.cs107.play.game.rpg.actor.FlyableEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.Collections;
import java.util.List;

public class FlameSkull extends Monster implements FlyableEntity, VulnerableActor {
    //Keeping the VulnerableActor implementation for tagging purposes, although it is unnecessary
    //as Monster already implements VulnerableActor.

    private final static float ATTACK_VALUE = 0.5f;
    private final static float FLAMESKULL_HP = 1.f;

    private final static int MIN_LIFE_TIME = 150;
    private final static int MAX_LIFE_TIME = 400;
    private final static int CHANGE_OF_ORIENT_PROBAB = 20;
    private int lifeTime;

    private final static int ANIM_DURATION = 6;
    private Animation[] aliveAnimation;
    private final static int MOVEMENT_SPEED = 4;
    private Animation currentAliveAnim;

    private final static Vulnerabilities[] VULNERABILITIES = {Vulnerabilities.PHYSICAL, Vulnerabilities.MAGIC};
    private ARPGInteractionVisitor handler = new FlamingSkullHandlerARPG();

    /**
     * Augmented Flameskull constructor, including set initial orientation
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, FLAMESKULL_HP, VULNERABILITIES);

        setAnimation();
        lifeTime = MIN_LIFE_TIME + RandomGenerator.getInstance().nextInt(MAX_LIFE_TIME - MIN_LIFE_TIME);
        currentAliveAnim = aliveAnimation[0];
    }

    /**
     * Default Flameskull constructor, randomly generated initial orientation
     *
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public FlameSkull(Area area, DiscreteCoordinates position) {
        this(area, Orientation.values()[RandomGenerator.getInstance().nextInt(4)], position);
    }

    @Override
    protected Animation getCurrentAnimation() {
        return currentAliveAnim;
    }

    /**
     * Creates the default (alive) animation for FlameSkull
     *
     * @return the animation
     */
    private void setAnimation() {
        Sprite[][] spritesAnim = RPGSprite.extractSprites("zelda/flameSkull", 3, 1.8f, 1.8f, this, 32, 32, new Orientation[]
                {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                spritesAnim[i][j].setAnchor(new Vector(-0.2f, 0f));
            }
        }

        aliveAnimation = RPGSprite.createAnimations(ANIM_DURATION / 2, spritesAnim);
    }

    @Override
    public void update(float deltaTime) {
        --lifeTime;

        if (lifeTime <= 0) {
            die();
        } else {
            orientate(orientateMonster(CHANGE_OF_ORIENT_PROBAB));
            if (isDisplacementOccurs()) {
                resetMotion();
            } else {
                //This test for debugging purposes
                if (getOwnerArea().canEnterAreaCells(this,getFieldOfViewCells())) {
                    move(MOVEMENT_SPEED);
                }
            }
        }
        currentAliveAnim = aliveAnimation[getOrientation().ordinal()];
        currentAliveAnim.update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        //No view interaction but useful for debugging
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    private class FlamingSkullHandlerARPG implements ARPGAggressiveVisitor {

        /**
         * (Tries to) attack the VulerableActor that may take damage if Fire is among its weaknesses
         *
         * @param target (VulnerableActor), not null
         */
        @Override
        public void interactWith(VulnerableActor target) {
            target.loseHp(ATTACK_VALUE, Vulnerabilities.FIRE);
        }

        /**
         * Attacks the player
         *
         * @param player (ARPGPlayer), not null
         */
        @Override
        public void interactWith(ARPGPlayer player) {
            player.loseHp(ATTACK_VALUE);
        }

        /**
         * Sets the grass to its cut state, engaging slicing animation
         *
         * @param grass (Grass), not null
         */
        @Override
        public void interactWith(Grass grass) {
            grass.setIsCut();
        }

        /**
         * Forces bomb's explosion
         *
         * @param bomb (Bomb), not null
         */
        @Override
        public void interactWith(Bomb bomb) {
            bomb.setForceExplode();
        }

    }
}
