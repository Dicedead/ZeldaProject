package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
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

public class MagicWaterProjectile extends Projectile {

    private final static int WATER_ANIM_TIME = 4;
    private final static float MAGIC_DAMAGE = 0.75f;
    private MagicWaterHandlerARPG handler;
    private Animation waterAnim;

    public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        handler = new MagicWaterHandlerARPG();
        waterAnim = createRepresentation();
    }

    @Override
    protected Graphics getRepresentation() {
        return waterAnim;
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

    private Animation createRepresentation() {
        Sprite[] magicWaterSprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            magicWaterSprites[i] = new RPGSprite("zelda/magicWaterProjectile", 1f, 1f, this, new
                    RegionOfInterest(i * 32, 0, 32, 32), Vector.ZERO, 1.f, -3);
        }
        return new Animation(WATER_ANIM_TIME, magicWaterSprites, true);
    }


    private class MagicWaterHandlerARPG implements ARPGAggressiveVisitor {

        /**
         * Attacks all nearby vulnerable actors with magic
         *
         * @param target (VulnerableActor), not null
         */
        public void interactWith(VulnerableActor target) {
            target.loseHp(MAGIC_DAMAGE, VulnerableActor.Vulnerabilities.MAGIC);
            MagicWaterProjectile.super.destroyProjectile();
        }

        @Override
        public void interactWith(FireSpell flames) {
            flames.setOutForced();
        }
    }
}
