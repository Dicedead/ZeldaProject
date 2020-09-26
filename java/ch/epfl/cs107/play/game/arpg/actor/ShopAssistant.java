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

public class ShopAssistant extends Assistant implements EnvironmentEntity {

    private final Sprite spriteShopAssistant = new RPGSprite("girl.1", 1.1f, 1.65f, this,
            new RegionOfInterest(48, 46, 16, 20), new Vector(-1, 0), 1.f, -10);

    private Keyboard keyboard;
    private boolean activeDialogs;
    private static Dialog dialogSeller;

    /**
     * Default ShopAssistant constructor including its dialogs
     *
     * @param area (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param position (DiscreteCoordinates): Initial position, not null
     */
    public ShopAssistant(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        activeDialogs = false;
        dialogSeller = new Dialog("For 100 coins, I can offer you 10 bombs. Press E to accept the trade or Space to cancel it","zelda/dialog",getOwnerArea());
    }

    public void setDialogs() {
        activeDialogs = !activeDialogs;
    }

    public Dialog getDialog_Seller() {
        return dialogSeller;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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
        spriteShopAssistant.draw(canvas);

        if(activeDialogs) {
            dialogSeller.draw(canvas);
            if (keyboard.get(Keyboard.V).isPressed()) {
                activeDialogs = false;
            }
        }
    }
}