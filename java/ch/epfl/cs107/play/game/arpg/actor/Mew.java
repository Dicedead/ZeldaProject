package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

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
import ch.epfl.cs107.play.signal.wave.Sine;
import ch.epfl.cs107.play.window.Canvas;

public class Mew extends CollectableAreaEntity {

    private float oscillator;
    private float intensity;
    private Sine sineWave;

    private final Sprite spriteMew = new RPGSprite("mew.fixed", 1.f, 1.f, this,
            new RegionOfInterest(0, 0, 16, 16), Vector.ZERO, 1.f, -10);

    public Mew(Area area, DiscreteCoordinates position) {
        super(area, position);
        sineWave = new Sine(1f,0.4f,0f);
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
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public void update(float deltaTime) {
        oscillator += deltaTime;
        super.update(deltaTime);
    }

    /**
     * Using setAnchor method and a sinewave to fake a castlekey animation
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        intensity = sineWave.getIntensity(oscillator);
        spriteMew.setAnchor(new Vector(intensity, 0));
        spriteMew.draw(canvas);
    }

    @Override
    protected void setAnim() {
    }

    @Override
    protected Animation getCurrentAnimation() {
        return null;
    }

}