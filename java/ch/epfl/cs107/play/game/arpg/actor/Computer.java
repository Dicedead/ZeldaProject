package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;

public class Computer extends Door {

    private Logic signal;
    private Keyboard keyboard;


    /**
     * Default Computer constructor
     *
     * @param destination          (String) : Destination area's string
     * @param otherSideCoordinates (DiscreteCoordinates) : Destination's coordinates
     * @param signal               (Logic) : Set if the signal is on or off
     * @param area                 (Area): Owner Area, not null
     * @param orientation          (Orientation): Initial player orientation, not null
     * @param position             (Coordinates): Initial position, not null
     */
    public Computer(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
                    Orientation orientation, DiscreteCoordinates position) {
        super(destination, otherSideCoordinates, signal, area, orientation, position);
        signal = Logic.FALSE;
    }

    public boolean isOpen() {
        return signal.isOn();
    }

    protected void setSignal(Logic signal) {
        this.signal = signal;
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