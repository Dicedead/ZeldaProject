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

public class CastleKey extends CollectableAreaEntity {

    private float oscillator;
    private float intensity;
    private Sine sineWave;

    private final Sprite spriteCastleKey = new RPGSprite("zelda/key", 1.f, 1f, this,
            new RegionOfInterest(0, 0, 16, 32), Vector.ZERO, 1.f, -10);

    /**
     * Default Castle Key constructor
     *
     * @param area        (Area) : Owner area, not null
     * @param position    (DiscreteCoordinates) : Initial position of the key : not null
     */

    public CastleKey(Area area, DiscreteCoordinates position) {
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

    public void update(float deltaTime) {
        oscillator += deltaTime;
        super.update(deltaTime);
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

    /**
     * Using setAnchor method and a sinewave to fake a castlekey animation
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        intensity = sineWave.getIntensity(oscillator);
        if (intensity < 0) {
            spriteCastleKey.setAnchor(new Vector(intensity, intensity));
        }
        else {
            spriteCastleKey.setAnchor(new Vector(intensity, -1*intensity));
        }
        spriteCastleKey.draw(canvas);
    }

}
