package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableDropperEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogMonster extends Monster implements VulnerableActor, CollectableDropperEntity {
    //Keeping the VulnerableActor implementation for tagging purposes, although it is unnecessary
    //as Monster already implements VulnerableActor.

    private final static float ATTACK_VALUE = 1.f;
    private final static float LOG_MONSTER_HP = 2.f;

    private final static int MAX_IDLE_DURATION = 90;
    private final static int MIN_SLEEPING_DURATION = 40;
    private final static int MAX_SLEEPING_DURATION = 120;
    private final static int CHANGE_ORIENT_PROBAB = 20;

    private int idleCount;
    private final static int DEFAULT_ANIM_DURATION = 6;
    private final static int SLEEPING_ANIM_DURATION = 8;
    private final static int WAKING_ANIM_DURATION = 5;
    private final static int MOVEMENT_SPEED = 10;
    private final static int IDLE_PROB = 15;
    private Animation[][] statesAnimations = new Animation[3][4];

    private final static Vulnerabilities[] VULNERABILITIES = {Vulnerabilities.PHYSICAL, Vulnerabilities.FIRE};
    private final static int FOV_CELLS_AHEAD = 8;
    private ARPGInteractionVisitor handler = new LogMonsterHandler();

    private Animation currentAliveAnim;
    private LogStates currentState;

    /**
     * Augmented LogMonster constructor, including set initial orientation
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
    public LogMonster(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, LOG_MONSTER_HP, VULNERABILITIES);

        setAnimation();
        currentState = LogStates.IDLE;
        idleCount = 0;
        currentAliveAnim = statesAnimations[0][getOrientation().ordinal()];
    }

    /**
     * Default LogMonster constructor, randomly generated initial orientation
     *
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public LogMonster(Area area, DiscreteCoordinates position) {
        this(area, Orientation.values()[RandomGenerator.getInstance().nextInt(4)], position);
    }

    private void becomeIdle(int duration) {
        idleCount = duration;
    }

    /**
     * Creates all of LogMonster's animations
     *
     * @return a 2 dimensional array storing these animations
     */
    private void setAnimation() {
        Vector anchor = new Vector(-0.6f, 0f);

        Sprite[][] defaultAnim = RPGSprite.extractSprites("zelda/logMonster",4, 2f,2f,
                this,32,32, new Vector(-0.5f,0f), new Orientation[]{Orientation.DOWN,
                        Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        Sprite[] sleepingSprites = new Sprite[4];
        Sprite[] wakingSprites = new Sprite[3];

        for (int i = 0; i < 4; ++i) {
            sleepingSprites[i] = new RPGSprite("zelda/logMonster.sleeping", 2f, 2f, this, new
                    RegionOfInterest(0, i * 32, 32, 32));

            for (int j = 0; j < 3; ++j) {
                if (i == 0) {
                    wakingSprites[j] = new RPGSprite("zelda/logMonster.wakingUp", 2f, 2f, this, new
                            RegionOfInterest(0, i * 32, 32, 32));
                }
                defaultAnim[i][j].setAnchor(anchor);
            }
        }

        statesAnimations[0] = RPGSprite.createAnimations(DEFAULT_ANIM_DURATION / 2, defaultAnim);
        statesAnimations[1][0] = new Animation(SLEEPING_ANIM_DURATION, sleepingSprites, true);
        statesAnimations[1][0].setAnchor(anchor);
        statesAnimations[2][0] = new Animation(WAKING_ANIM_DURATION, wakingSprites, false);
        statesAnimations[2][0].setAnchor(anchor);
    }

    @Override
    public void update(float deltaTime) {

        if (idleCount > 0) {
            --idleCount;
        }

        switch (currentState) {
            case IDLE:
                if (!isDisplacementOccurs() && idleCount == 0) {
                    orientate(orientateMonster(CHANGE_ORIENT_PROBAB));
                    move(MOVEMENT_SPEED);
                    if (RandomGenerator.getInstance().nextInt(100) < IDLE_PROB) {
                        becomeIdle(RandomGenerator.getInstance().nextInt(MAX_IDLE_DURATION));
                    }
                    statesAnimations[0][getOrientation().ordinal()].reset();
                } else {
                    currentAliveAnim = statesAnimations[0][getOrientation().ordinal()];
                }
                break;

            case ATTACKING:
                move(MOVEMENT_SPEED / 2);
                currentAliveAnim = statesAnimations[0][getOrientation().ordinal()];
                if (!isDisplacementOccurs() && !getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells())) {
                    currentState = LogStates.ABOUT_TO_SLEEP;
                    statesAnimations[0][getOrientation().ordinal()].reset();
                }
                break;

            case ABOUT_TO_SLEEP:
                becomeIdle(MIN_SLEEPING_DURATION + RandomGenerator.getInstance().nextInt(MAX_SLEEPING_DURATION -
                        MIN_SLEEPING_DURATION));
                currentState = LogStates.SLEEPING;
                break;

            case SLEEPING:
                currentAliveAnim = statesAnimations[1][0];
                if (idleCount <= 0) {
                    currentState = LogStates.WAKING;
                    statesAnimations[1][0].reset();
                }
                break;

            case WAKING:
                currentAliveAnim = statesAnimations[2][0];
                if (currentAliveAnim.isCompleted()) {
                    currentState = LogStates.IDLE;
                    statesAnimations[2][0].reset();
                }
                break;
        }

        if (idleCount <= 0 || currentState == LogStates.SLEEPING) {
            currentAliveAnim.update(deltaTime);
        }
        super.update(deltaTime);
    }

    @Override
    protected Animation getCurrentAnimation() {
        return currentAliveAnim;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (currentState == LogStates.ATTACKING) {
            return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        } else {
            List<DiscreteCoordinates> fovCells = new ArrayList<>();
            for (int i = 1; i <= FOV_CELLS_AHEAD; ++i) {
                fovCells.add(getCurrentMainCellCoordinates().jump(
                        ((int) getOrientation().toVector().x) * i,
                        ((int) getOrientation().toVector().y) * i));
            }
            return fovCells;
        }
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        if (currentState == LogStates.IDLE || currentState == LogStates.ATTACKING) {
            other.acceptInteraction(handler);
        }
    }

    public Droppables dropItem() {
        return Droppables.COIN;
    }

    private enum LogStates {
        IDLE,
        ATTACKING,
        ABOUT_TO_SLEEP,
        SLEEPING,
        WAKING,;
    }

    private class LogMonsterHandler implements ARPGInteractionVisitor {

        /**
         * Attacks the player
         *
         * @param player (ARPGPlayer), not null
         */
        @Override
        public void interactWith(ARPGPlayer player) {
            currentState = LogStates.ATTACKING;
            if (getFieldOfViewCells().contains(player.getCurrentCells().get(0))) {
                player.loseHp(ATTACK_VALUE);
            }
        }
    }
}

