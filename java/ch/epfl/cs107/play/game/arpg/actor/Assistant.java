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

public class Assistant extends AreaEntity implements EnvironmentEntity {

    private final Sprite spriteAssistant = new RPGSprite("assistant.fixed", 1.f, 1.5f, this,
            new RegionOfInterest(0, 0, 16, 32), Vector.ZERO, 1.f, -10);

    private boolean activeDialogs;
    private Dialog dialogBeginning;
    private Dialog dialogElse;
    private Keyboard keyboard;
    private int countOfInteractions = 0;

    /**
     * Default Assistant constructor including its dialogs
     *
     * @param area(Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param position    (DiscreteCoordinates): Initial position, not null
     */
    public Assistant(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        activeDialogs = false;
        dialogBeginning = new Dialog("As a good luck present, I offer you 169 golden coins and 10 bombs ! Stay safe my son.", "zelda/dialog", getOwnerArea());
        dialogElse = new Dialog("I heard someone was coming to save us from the dangerous entities. I wish he could be there sooner.", "zelda/dialog", getOwnerArea());
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
        ++countOfInteractions;
    }

    @Override
    public void draw(Canvas canvas) {
        spriteAssistant.draw(canvas);

        if (activeDialogs) {
            if (countOfInteractions == 1) {
                dialogBeginning.draw(canvas);
            } else {
                dialogElse.draw(canvas);
            }

            if (keyboard.get(Keyboard.V).isPressed()) {
                activeDialogs = false;
            }
        }
    }
}
