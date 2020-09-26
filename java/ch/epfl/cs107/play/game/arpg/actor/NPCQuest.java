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

public class NPCQuest extends Assistant implements EnvironmentEntity {


    private final Sprite spriteNPCQuest = new RPGSprite("flora", 1.f, 2f, this,
            new RegionOfInterest(0, 42, 16, 32), new Vector(0, 1), 1.f, 100);
    private Keyboard keyboard;
    private boolean activeDialogs;
    private static Dialog dialogQuest;
    private static Dialog dialogReward;
    private boolean hasMew;

    /**
     * Default NPCQuest constructor including its dialogs
     *
     * @param area (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param position (DiscreteCoordinates): Initial position, not null
     */
    public NPCQuest(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        activeDialogs = false;
        hasMew = false;
        dialogQuest = new Dialog("I have lost my ICmon Mew somewhere. He's probably in a dangerous area and I need your help...", "zelda/dialog", getOwnerArea());
        dialogReward = new Dialog("Oh my god ! I'm so thankful ! As promise, here's your reward : 500 coins", "zelda/dialog", getOwnerArea());
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * Boolean used to set when the dialogs are active
     */
    public void setDialogs() {
        activeDialogs = !activeDialogs;
    }


    public boolean hasMew() {
        hasMew = true;
        return hasMew;
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
        spriteNPCQuest.draw(canvas);

        if (activeDialogs) {
            if (hasMew) {
                dialogReward.draw(canvas);
            } else {
                dialogQuest.draw(canvas);
            }

            if (keyboard.get(Keyboard.V).isPressed()) {
                activeDialogs = false;
            }
        }
    }
}