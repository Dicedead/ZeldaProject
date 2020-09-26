package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.EnvironmentEntity;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class King extends AreaEntity implements EnvironmentEntity {


    private final Sprite spriteKing = new RPGSprite("zelda/king", 1.f, 2.5f, this,
            new RegionOfInterest(0, 60, 16, 32), Vector.ZERO, 1.f, -10);

    private boolean activeDialogs;
    private static Dialog dialogSaved;
    private Keyboard keyboard;


    /**
     * Default King constructor
     *
     * @param area        (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param position (DiscreteCoordinates): Initial position, not null
     */
    public King(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        activeDialogs = false;
        dialogSaved = new Dialog("Me and the kingdom will be forever grateful and we will remember forever your heroic behavior !", "zelda/dialog", getOwnerArea());
    }

    public void setDialogs() {
        activeDialogs = !activeDialogs;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.E).isPressed();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }


    @Override
    public void draw(Canvas canvas) {
        spriteKing.draw(canvas);

        if (activeDialogs) {

            dialogSaved.draw(canvas);

            if (keyboard.get(Keyboard.SPACE).isPressed()) {
                activeDialogs = false;
            }
        }
    }
}
