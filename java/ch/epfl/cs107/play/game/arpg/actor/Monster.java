package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableDropperEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Monster extends MovableAreaEntity implements Interactor, VulnerableActor {

    private float hp;
    private static final float DEFAULT_MAX_HP = 3.f;
    private static final int TAKING_DAMAGE_RESET = 12;
    private int takingDamage;
    private boolean isDead;
    private boolean hasDropped;
    private Vulnerabilities[] weaknesses;

    private static final int VANISHING_DURATION = 2;
    private Animation vanishingAnim;

    /**
     * Augmented Monster constructor, including non default health points
     *
     * @param area            (Area): Owner area. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     * @param orientation     (Orientation): Initial orientation of the entity. Not null
     * @param setHp           (float): Initial health. Not null
     * @param vulnerabilities (Vulnerabilities): this's weaknesses
     */
    public Monster(Area area, Orientation orientation, DiscreteCoordinates position, float setHp,
                   Vulnerabilities... vulnerabilities) {
        super(area, orientation, position);
        Sprite[] vanishingSprites = new Sprite[7];
        for (int i = 0; i < 7; ++i) {
            vanishingSprites[i] = new RPGSprite("zelda/vanish", 1, 1, this, new
                    RegionOfInterest(i * 32, 0, 32, 32), Vector.ZERO, 1.f, -2);
            vanishingSprites[i].setAnchor(new Vector(-0.5f, 0));
        }
        vanishingAnim = new Animation(VANISHING_DURATION, vanishingSprites, false);

        weaknesses = vulnerabilities;
        hp = setHp;

        isDead = false;
        hasDropped = false;

        takingDamage = TAKING_DAMAGE_RESET;
    }

    /**
     * Default Monster constructor (augmented minus hp and animation duration)
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     * @param vulnerabilities (Vulnerabilities): this's weaknesses
     */
    public Monster(Area area, Orientation orientation, DiscreteCoordinates position, Vulnerabilities... vulnerabilities) {
        this(area, orientation, position, DEFAULT_MAX_HP, vulnerabilities);
    }

    /**
     * Mainly updates animation, dead or alive status, and immunity/cooldown times
     *
     * @param deltaTime (float)
     */
    @Override
    public void update(float deltaTime) {

        if (0 < takingDamage && takingDamage != TAKING_DAMAGE_RESET) {
            --takingDamage;
        } else {
            takingDamage = TAKING_DAMAGE_RESET;
        }

        if (hp <= 0.f || isDead) {
            isDead = true;
            vanishingAnim.update(deltaTime);
            return;
            //Prevents vanishing animation movements
        }
        super.update(deltaTime);
    }

    void die() {
        isDead = true;
    }

    abstract protected Animation getCurrentAnimation();

    /**
     * Randomly determines whether to change orientation or not, then if the first test is positive,
     * randomly selects one of the three orientations the monster did NOT initially have
     * -selecting from all 4 would have made movements too chaotic
     *
     * @return mentionned orientation
     */
    protected Orientation orientateMonster(int changeProbab) {

        if (RandomGenerator.getInstance().nextInt(100) < changeProbab) {
            Orientation[] changeDirections = new Orientation[3];
            int increm = 0;
            for (int i = 0; i < 4; ++i) {
                if (Orientation.values()[i] != getOrientation()) {
                    changeDirections[increm] = Orientation.values()[i];
                    ++increm;
                }
            }
            return changeDirections[RandomGenerator.getInstance().nextInt(3)];
        } else {
            return getOrientation();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isDead) {
            endingAnimationAndUnregister(vanishingAnim, canvas, getOwnerArea(), this);

            if (this instanceof CollectableDropperEntity) {
                if (!hasDropped) {
                    CollectableDropperEntity.Droppables droppedItem = ((CollectableDropperEntity) this).dropItem();
                    switch (droppedItem) {
                        case COIN:
                            getOwnerArea().registerActor(new Coin(getOwnerArea(), getCurrentCells().get(0)));
                            break;
                        case HEART:
                            getOwnerArea().registerActor(new Heart(getOwnerArea(), getCurrentCells().get(0)));
                            break;
                        case CHERRY:
                            getOwnerArea().registerActor(new Cherry(getOwnerArea(), getCurrentCells().get(0)));
                            break;
                        case CASTLEKEY:
                            getOwnerArea().registerActor(new CastleKey(getOwnerArea(), getCurrentCells().get(0)));
                            break;
                    }
                    hasDropped = true;
                }
            }

        } else {
            if (getCurrentAnimation() != null) {
                getCurrentAnimation().draw(canvas);
            }
        }
    }


    @Override
    public boolean takeCellSpace() {
        return !isDead;
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
        ((ARPGInteractionVisitor) v).interactWith((VulnerableActor) this);
    }

    /**
     * Removing some of the monster's hp if they are vulnerable to the attacks they're enduring
     * -NOTE: Although it is not the case in the project, some monsters may have an attack
     * that corresponds to two vulnerabilities, hence the second for each loop
     * implementing this possibility.
     *
     * @param deltaHp     (float): to be removed from Monster's hp
     * @param attackTypes (Vulnerabilities): the type of the attacks
     */
    public void loseHp(float deltaHp, Vulnerabilities... attackTypes) {
        if (takingDamage == TAKING_DAMAGE_RESET) {
            for (Vulnerabilities weakness : weaknesses) {
                for (Vulnerabilities attack : attackTypes) {
                    if (attack.equals(weakness)) {
                        --takingDamage;
                        this.hp -= deltaHp;
                        return;
                    }
                }
            }
        }
    }
}
