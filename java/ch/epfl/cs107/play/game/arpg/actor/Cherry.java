package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.wave.Sine;
import ch.epfl.cs107.play.window.Canvas;

public class Cherry extends CollectableAreaEntity implements Interactable{

    final static float REGEN = 0.5f;

    private float oscillator;
    private float intensity;
    private Sine sineWave;

    private final Sprite spriteCherry = new RPGSprite("Cherry", 1.f, 1.f, this,
            new RegionOfInterest(0, 0, 256, 256), Vector.ZERO, 1.f, -10);

    public Cherry(Area area, DiscreteCoordinates position) {
        super(area, position);

        sineWave = new Sine(1f,0.4f,0f);
    }

    @Override
    protected void setAnim() {
    }

    @Override
    protected Animation getCurrentAnimation() {
        return null;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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

    public void update(float deltaTime) {
        oscillator += deltaTime;
        super.update(deltaTime);
    }

    /**
     * Using setAnchor method and a sinewave to fake a cherry animation
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        intensity = sineWave.getIntensity(oscillator)/2f;
       spriteCherry.setAnchor(new Vector(0, intensity));
        spriteCherry.draw(canvas);
    }

}