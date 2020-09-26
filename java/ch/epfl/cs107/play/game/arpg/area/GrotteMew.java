package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Chest;
import ch.epfl.cs107.play.game.arpg.actor.Torch;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class GrotteMew extends ARPGArea {

    private String code;
    private String chestCode;
    private Chest chest;
    private Torch torch1;
    private Torch torch2;
    private Torch torch3;
    private Torch torch4;
    private Torch torch5;

    @Override
    public String getTitle() {
        return "GrotteMew";
    }

    @Override
    protected void createArea() {
        Door door1 = new Door("Grotte", new DiscreteCoordinates(6, 31), Logic.TRUE, this, Orientation.DOWN,
                new DiscreteCoordinates(8, 2));

        chest = new Chest(this, Orientation.DOWN, new DiscreteCoordinates(8, 6), "15342");

        torch3 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(8, 12));
        torch2 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(2, 7));
        torch4 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(14, 7));
        torch5 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(11, 2));
        torch1 = new Torch(this, Orientation.DOWN, new DiscreteCoordinates(5, 2));

        chestCode = chest.getChestCode();
        code = "";

        registerActors(door1, chest, torch1, torch2, torch3, torch4, torch5, new Background(this));
        //Calling registerActors method in ARPGArea
    }

    @Override
    public void update(float deltaTime) {
        if (torch1.getSignal() == Logic.TRUE && !torch1.getUsage()) {
            code += '1';
            torch1.setUsage();
        }

        if (torch2.getSignal() == Logic.TRUE && !torch2.getUsage()) {
            code += '2';
            torch2.setUsage();
        }

        if (torch3.getSignal() == Logic.TRUE && !torch3.getUsage()) {
            code += '3';
            torch3.setUsage();
        }

        if (torch4.getSignal() == Logic.TRUE && !torch4.getUsage()) {
            code += '4';
            torch4.setUsage();
        }

        if (torch5.getSignal() == Logic.TRUE && !torch5.getUsage()) {
            code += '5';
            torch5.setUsage();
        }
        openChest();

        super.update(deltaTime);
    }

    public void openChest() {
        if (code != null && code.equals(chestCode)) {
            chest.setIsOpenable();
            code = "";
        }
        assert code != null;
        if (code.length() == chestCode.length() && !code.equals(chestCode)) {
            torch1.setOff();
            torch2.setOff();
            torch3.setOff();
            torch4.setOff();
            torch5.setOff();
            code = "";
        }
    }
}