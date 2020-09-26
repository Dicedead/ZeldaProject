package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGAggressiveVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor {

    private static final int DEFAULT_COUNTDOWN = 90;
    private static final int COUNTDOWN_MIN = 40;
    private static final int COUNTDOWN_MAX = 140;

    private static final int EXPLODING_DURATION = 2;
    private static final float BOMB_DAMAGE = 1.5f;
    private int countdown;
    private int countdownMemory;
    private int speedAnimIncrement;

    private boolean forceExplode;
    private boolean isExploding;

    private Animation flashingAnimation;
    private Animation explodingAnimation;

    private ARPGInteractionVisitor handler = new BombHandlerARPG();

    /**
     * Augmented Bomb constructor, including its flashing animation sprites and countdown
     *
     * @param area            (Area): Owner area. Not null
     * @param position        (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param countdownLength (int): to set if not default. Not null
     */
    public Bomb(Area area, DiscreteCoordinates position, int countdownLength) {
        super(area, Orientation.UP, position);

        countdown = countdownLength;
        countdownMemory = countdown;

        Sprite[] flashingSprites = new Sprite[2];
        for (int i = 0; i < 2; ++i) {
            flashingSprites[i] = new RPGSprite("zelda/bomb", 1, 1, this, new
                    RegionOfInterest(i * 16, 0, 16, 32), new Vector(0f, 0.25f), 1.f, -5);
        }

        flashingAnimation = new Animation(countdownLength * 2, flashingSprites, true);
        speedAnimIncrement = 5;

        forceExplode = false;
        isExploding = false;

        Sprite[] explodingSprites = new Sprite[7];
        for (int i = 0; i < 7; ++i) {
            explodingSprites[6 - i] = new RPGSprite("zelda/explosion", 2, 2, this, new
                    RegionOfInterest(i * 32, 0, 32, 32), new Vector(-0.5f, -0.5f), 1.f, -5);
        }
        explodingAnimation = new Animation(EXPLODING_DURATION, explodingSprites, false);

    }

    /**
     * Default Bomb constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bomb(Area area, DiscreteCoordinates position) {
        this(area, position, DEFAULT_COUNTDOWN);
    }

    /**
     * Random countdown Bomb constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param random      (boolean): true -> random bounded countdown value, false -> default
     */
    public Bomb(Area area,  DiscreteCoordinates position, boolean random) {
        this(area, position,
                (random) ? COUNTDOWN_MIN + RandomGenerator.getInstance().
                        nextInt(COUNTDOWN_MAX - COUNTDOWN_MIN) : DEFAULT_COUNTDOWN);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * @return (ArrayList) of the 8 surrounding cells + the current cell
     * NOTE: NOT using getNeighbours() as we want the bomb to explode in a square and not cross shaped
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {

        List<DiscreteCoordinates> surroundingCells = new ArrayList<>();
        DiscreteCoordinates bottomLeftCoords = getCurrentMainCellCoordinates().jump(-1, -1);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                surroundingCells.add(bottomLeftCoords.jump(i, j));
            }
        }
        //Index 4: current cell
        surroundingCells.remove(4);

        return surroundingCells;
    }

    /**
     * Updates countdown, animation and explosion states
     * -the bomb flashes more and more swiftly as its explosion approaches,
     * updating the animation
     *
     * @param deltaTime (float)
     */
    @Override
    public void update(float deltaTime) {
        if (countdown > 0 && !forceExplode) {
            --countdown;
            --countdownMemory;
            flashingAnimation.setSpeedFactor(speedAnimIncrement);
            flashingAnimation.update(deltaTime);
            ++speedAnimIncrement;
        } else {
            //Avoids dealing damage more than once to VulnerableActors and ARPGPlayer
            //by having this condition true only the very first time this block is
            //reached, as countdownMemory and countdown are decremented at the same
            //rate up until this point, where countdown increments.

            //Note that we cannot use forceExplode as we need to dissociate the animation
            //updates from the interactive updates at this stage.
            isExploding = (countdownMemory == countdown);
            ++countdown;

            forceExplode = true;
            explodingAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {

        if (forceExplode) {
            endingAnimationAndUnregister(explodingAnimation, canvas, getOwnerArea(), this);
        } else {
            flashingAnimation.draw(canvas);
        }
    }

    /**
     * Used to force instant explosion (hit by arrow, by flameskull,...)
     */
    public void setForceExplode() {
        forceExplode = true;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
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
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    private class BombHandlerARPG implements ARPGAggressiveVisitor {

        /**
         * Sets the grass to its cut state, engaging slicing animation
         *
         * @param grass (Grass), not null
         */
        @Override
        public void interactWith(Grass grass) {
            if (forceExplode && isExploding) {
                grass.setIsCut();
            }
        }

        /**
         * Chain reaction: explodes bombs
         *
         * @param bomb (Bomb), not null
         */
        @Override
        public void interactWith(Bomb bomb) {
            if (forceExplode && isExploding) {
                bomb.setForceExplode();
            }
        }

        /**
         * Attacks all nearby vulnerable actors with physicality
         *
         * @param target (VulnerableActor), not null
         */
        @Override
        public void interactWith(VulnerableActor target) {
            if (forceExplode && isExploding) {
                target.loseHp(BOMB_DAMAGE, VulnerableActor.Vulnerabilities.PHYSICAL);
            }
        }

        /**
         * Damages the player
         *
         * @param player (ARPGPlayer), not null
         */
        @Override
        public void interactWith(ARPGPlayer player) {
            if (forceExplode && isExploding) {
                player.loseHp(BOMB_DAMAGE);
            }
        }

        /**
         * Sets the rock to its imploded state, engaging implosion animation
         *
         * @param rock (Rock), not null
         */
        @Override
        public void interactWith(Rock rock) {
            if (forceExplode && isExploding) {
                rock.setIsImploded();
            }
        }

        @Override
        public void interactWith(CherryTree cherryTree) {
            if (forceExplode && isExploding) {
                cherryTree.setIsCut();
            }
        }
    }
}

