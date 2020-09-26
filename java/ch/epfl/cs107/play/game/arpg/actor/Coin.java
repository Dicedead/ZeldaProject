package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Coin extends CollectableAreaEntity {

    private final static int SPIN_SPEED = 4;
    public final static int VALUE = 40;
    private Animation anim;

    public Coin(Area area, DiscreteCoordinates position) {
        super(area, position);
    }

    @Override
    protected void setAnim() {
        Sprite[] sprites = new Sprite[4];
        for (int i = 0; i < 4; ++i) {
            sprites[i] = new RPGSprite("zelda/coin", 0.75f, 0.75f, this, new
                    RegionOfInterest(i * 16, 0, 16, 16), Vector.ZERO, 1.f, -7);
        }
        anim = new Animation(SPIN_SPEED, sprites, true);
        anim.setAnchor(new Vector(0.2f,0));
    }

    @Override
    protected Animation getCurrentAnimation() {
        return anim;
    }

    @Override
    public void draw(Canvas canvas) {
        anim.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
