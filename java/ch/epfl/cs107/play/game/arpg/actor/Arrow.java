package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Projectile;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGAggressiveVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;

public class Arrow extends Projectile {

    private ArrowHandlerARPG handler;
    private final static float ARROW_DAMAGE = 0.5f;
    private Sprite arrowSprite;

    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        handler = new ArrowHandlerARPG();
        arrowSprite = createRepresentation();
    }

    @Override
    protected Graphics getRepresentation() {
        return arrowSprite;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    /**
     * Generic interaction call
     *
     * @param other (Interactable). Not null
     */
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }


    private Sprite createRepresentation() {
        Sprite[] arrowSprite = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            arrowSprite[i] = new RPGSprite("zelda/arrow", 1.f, 1.5f, this,
                    new RegionOfInterest(32 * i, 0, 32, 32), Vector.ZERO, 1.f, -10);
        }
        return arrowSprite[getOrientation().ordinal()];
    }

    private class ArrowHandlerARPG implements ARPGAggressiveVisitor {

        /**
         * Sets the grass to its cut state, engaging slicing animation
         *
         * @param grass (Grass), not null
         */
        public void interactWith(Grass grass) {
            grass.setIsCut();
            Arrow.super.destroyProjectile();
        }

        public void interactWith(Bomb bomb) {
            bomb.setForceExplode();
            Arrow.super.destroyProjectile();
        }

        /**
         * Attacks all nearby vulnerable actors with physicality
         *
         * @param target (VulnerableActor), not null
         */
        public void interactWith(VulnerableActor target) {
            target.loseHp(ARROW_DAMAGE, VulnerableActor.Vulnerabilities.PHYSICAL);
            Arrow.super.destroyProjectile();
        }

        /**
         * Damages the player - because why not
         *
         * @param player (ARPGPlayer), not null
         */
        public void interactWith(ARPGPlayer player) {
            player.loseHp(ARROW_DAMAGE);
            Arrow.super.destroyProjectile();
        }

        /**
         * Extinguishes flamespells
         *
         * @param flames (FlameSpell)
         */
        public void interactWith(FireSpell flames) {
            flames.setOutForced();
        }

        public void interactWith(Orb orb) {
            if (orb.isDefaultState()) {
                orb.setOpenBridge();
            }
        }
    }
}