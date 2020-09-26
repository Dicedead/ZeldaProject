package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.EnvironmentEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Staff extends CollectableAreaEntity implements EnvironmentEntity {

    private static final int ANIM_SPEED = 4;
    private Animation anim;

    public Staff(Area area, DiscreteCoordinates position) {
        super(area, position);
    }

    @Override
    protected void setAnim() {
        Sprite[] sprites = new Sprite[8];
        for (int i = 0; i < 8; ++i) {
            sprites[i] = new RPGSprite("zelda/staff",1.6f,1.6f,this,
                    new RegionOfInterest(32*i,0,32,32), Vector.ZERO,1f,-5);
        }
        anim = new Animation(ANIM_SPEED, sprites);
        anim.setAnchor(new Vector(-0.28f,0.1f));
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
