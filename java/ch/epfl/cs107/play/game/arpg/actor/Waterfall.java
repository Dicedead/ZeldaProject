package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.EnvironmentEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Waterfall extends AreaEntity implements EnvironmentEntity {
    //

    private Animation waterFallingAnimation;
    private final static int WATER_ANIM_TIME = 2;

    /**
     * Default Waterfall constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Waterfall(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        Sprite[] waterfallSprites = new Sprite[3];
        for (int i = 0; i < 3; ++i) {
            waterfallSprites[i] = new RPGSprite("zelda/waterfall", 4f, 4.73f, this, new
                    RegionOfInterest(i * 64, 0, 64, 64), new Vector(0f,-0.16f), 1.f, -10);
        }
        waterFallingAnimation = new Animation(WATER_ANIM_TIME, waterfallSprites, true);

    }

    public void update(float deltaTime) {
        waterFallingAnimation.update(deltaTime);
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
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }

    @Override
    public void draw(Canvas canvas) {
        waterFallingAnimation.draw(canvas);
    }
}