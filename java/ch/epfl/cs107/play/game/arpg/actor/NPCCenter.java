package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
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

public class NPCCenter extends Assistant implements EnvironmentEntity {

    private final Sprite spriteNPCCenter = new RPGSprite("joel.fixed", 1.f, 1.5f, this,
            new RegionOfInterest(0, 0, 16, 32), new Vector(0, 1), 1.f, -10);
    private Keyboard keyboard;
    private boolean activeDialogs;
    private Dialog dialogHealer;
    private Dialog dialogChjop;
    private boolean canHeal;


    /**
     * Default NPC Center constructor including its dialogs
     *
     * @param area        (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param position    (DiscreteCoordinates): Initial position, not null
     */
    public NPCCenter(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        activeDialogs = false;
        dialogHealer = new Dialog("Your health has been restaured and here are 2 cherries in case of emergency.", "zelda/dialog", getOwnerArea());
        dialogChjop = new Dialog("You are already full health. Go beat the monsters outside!", "zelda/dialog", getOwnerArea());
        canHeal = false;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public void setDialogs() {
        activeDialogs = true;
    }

    public boolean setCanHeal() {
        return canHeal = true;
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
        spriteNPCCenter.draw(canvas);

        if (activeDialogs) {
            if (canHeal) {
                dialogHealer.draw(canvas);
            } else {
                dialogChjop.draw(canvas);
            }
            if (keyboard.get(Keyboard.V).isPressed()) {
                activeDialogs = false;
            }
        }
    }
}