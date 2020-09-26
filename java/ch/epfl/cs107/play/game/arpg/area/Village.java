package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGSign;
import ch.epfl.cs107.play.game.arpg.actor.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.NPCQuest;
import ch.epfl.cs107.play.game.arpg.actor.Policeman;
import ch.epfl.cs107.play.game.arpg.actor.PressurePlate;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Village extends ARPGArea {

    @Override
    protected void createArea() {

        Door door1 = new Door("zelda/Ferme", new DiscreteCoordinates(4, 1), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(4, 19), new DiscreteCoordinates(5, 19));
        Door door2 = new Door("zelda/Ferme", new DiscreteCoordinates(14, 1), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(13, 19), new DiscreteCoordinates(14, 19),new DiscreteCoordinates(15, 19));
        Door door3 = new Door("zelda/Route", new DiscreteCoordinates(9, 1), Logic.TRUE, this, Orientation.UP,
                new DiscreteCoordinates(29, 19), new DiscreteCoordinates(30, 19));
        Door door5 = new Door("PetalburgCenter", new DiscreteCoordinates(6,1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(10,7));
        Door door6 = new Door("PetalburgShop", new DiscreteCoordinates(4,1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(25,12));

        PressurePlate pressure = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(15,18));
        CaveDoor caveDoor = new CaveDoor("Grotte",new DiscreteCoordinates(16, 4), this, Orientation.UP, pressure,
                new DiscreteCoordinates(25,18));
        NPCQuest quest = new NPCQuest(this,Orientation.DOWN,new DiscreteCoordinates(17,10));
        ARPGSign signShop = new ARPGSign("~~~~ Welcome to the Chj�p shop ~~~~   ~~~~ One of the most attractive        place of the Village ! ~~~~",this,Orientation.DOWN,new DiscreteCoordinates(27,10));
        ARPGSign signQuest = new ARPGSign("~~~ Welcome to the market Place ~~~        You can talk about almost everything here and meet new people",this,Orientation.DOWN,new DiscreteCoordinates(20,10));
        ARPGSign signHospital = new ARPGSign("~~~~ Welcome to the Hospital ~~~~   ~~~~ Come here to restore your health and to prepare yourself ~~~~",this,Orientation.DOWN,new DiscreteCoordinates(13,6));
        Policeman policeman1 = new Policeman(this,new DiscreteCoordinates(27,15));

        registerActors(door1,door2,door3,door5,door6, pressure, caveDoor,quest,signShop, signQuest,signHospital, policeman1,new Background(this), new Foreground(this));
        //Calling registerActors method in ARPGArea
    }

    @Override
    public String getTitle() {
        return "zelda/Village";
    }
}