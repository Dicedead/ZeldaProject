package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.Sign;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ARPGSign extends Sign {

    private String textMessage;
    private Keyboard keyboard;
    private boolean activeDialogs;
    private static Dialog dialogSign;

    private final Sprite spriteSign = new RPGSprite("zelda/Sign", 1.f, 1.f, this,
            new RegionOfInterest(0, 0, 17, 32), Vector.ZERO, 1.f, -10);


    /**
     * Default Sign constructor
     *
     * @param textMessage (String): Text message of the panel, not null
     * @param area        (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the entity, not null
     * @param position    (DiscreteCoordinate): Initial position of the entity, not null
     */
    public ARPGSign(String textMessage, Area area, Orientation orientation, DiscreteCoordinates position) {
        super(textMessage, area, orientation, position);
        this.textMessage = textMessage;
        activeDialogs = false;
        dialogSign = new Dialog(textMessage, "zelda/dialog", getOwnerArea());
    }


    public void setDialogs() {
        activeDialogs = !activeDialogs;
    }

    /// Sign extends AreaEntity

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    /// Door implements Graphics

    @Override
    public void draw(Canvas canvas) {

        spriteSign.draw(canvas);

        if (activeDialogs) {
            dialogSign.draw(canvas);
            dialogSign.resetDialog(textMessage);
            if (keyboard.get(Keyboard.V).isPressed()) {
                activeDialogs = false;
            }
        }
    }

    /// Door Implements Interactable

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

}
