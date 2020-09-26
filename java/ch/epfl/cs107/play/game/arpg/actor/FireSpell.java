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

import java.util.Collections;
import java.util.List;

public class FireSpell extends AreaEntity implements Interactor {

    private final static float DAMAGE_VALUE = 0.75f;
    private int force;
    private boolean outForced; //For fire vs fire interaction
    private final static VulnerableActor.Vulnerabilities FIRE_ATTACK = VulnerableActor.Vulnerabilities.FIRE;
    private FireSpellHandlerARPG handler;

    private final static int MIN_LIFETIME = 50;
    private final static int MAX_LIFETIME = MIN_LIFETIME + 150;
    private int lifetime;
    private final static int PROPAGATION_TIME_CYCLE = MIN_LIFETIME + 30;
    private int cycleTime;

    private final static int ANIMATION_DURATION = 4;
    private Animation fireAnim;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int setForce) {
        super(area, orientation, position);

        Sprite[] fireSprites = new Sprite[7];
        for (int i = 0; i < 7; ++i) {
            fireSprites[i] = new RPGSprite("zelda/fire", 1, 1, this, new RegionOfInterest(i * 16, 0, 16, 16),
                    Vector.ZERO, 1, -10);
        }
        fireAnim = new Animation(ANIMATION_DURATION,fireSprites);

        force = setForce;
        outForced = false;
        handler = new FireSpellHandlerARPG();

        lifetime = MIN_LIFETIME + RandomGenerator.getInstance().nextInt(MAX_LIFETIME - MIN_LIFETIME);
        cycleTime = PROPAGATION_TIME_CYCLE;
    }

    public void update(float deltaTime) {
        if (outForced) {
            getOwnerArea().unregisterActor(this);
        }

        --lifetime;
        --cycleTime;
        if (lifetime > 0) {
            if (cycleTime == 0 && force > 0 && getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells())) {
                getOwnerArea().registerActor(new FireSpell(getOwnerArea(), getOrientation(),
                        getFieldOfViewCells().get(0), force - 1));
                cycleTime = PROPAGATION_TIME_CYCLE;
            }
            fireAnim.update(deltaTime);
        } else {
            getOwnerArea().unregisterActor(this);
        }
        super.update(deltaTime);
    }

    protected void setOutForced() {
        outForced = true;
    }

    @Override
    public void draw(Canvas canvas) {
        fireAnim.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
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
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
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

    private class FireSpellHandlerARPG implements ARPGAggressiveVisitor {

        @Override
        public void interactWith(VulnerableActor enemy) {
            enemy.loseHp(DAMAGE_VALUE, FIRE_ATTACK);
        }

        @Override
        public void interactWith(ARPGPlayer player) {
            player.loseHp(DAMAGE_VALUE);
        }

        @Override
        public void interactWith(Bomb bomb) {
            bomb.setForceExplode();
        }

        @Override
        public void interactWith(Grass grass) {
            grass.setIsCut();
        }

        @Override
        public void interactWith(CherryTree cherry) {
            cherry.setIsCut();
        }

        /**
         * Little bonus interaction: Fire vs Fire on the same cell: the fire with the highest force survives
         *
         * @param otherFire (FireSpell)
         */
        @Override
        public void interactWith(FireSpell otherFire) {
            if (otherFire != FireSpell.this) {
                if (otherFire.force < FireSpell.this.force) {
                    otherFire.setOutForced();
                } else {
                    FireSpell.this.setOutForced();
                }
            }
        }
    }
}
