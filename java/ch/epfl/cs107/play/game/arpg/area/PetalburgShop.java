package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGSign;
import ch.epfl.cs107.play.game.arpg.actor.ShopAssistant;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class PetalburgShop extends ARPGArea {

    private ShopAssistant shopAssistant;

    @Override
    public String getTitle() {
        return "PetalburgShop";
    }

    @Override
    protected void createArea() {
        Door door1 = new Door("zelda/Village", new DiscreteCoordinates(25,11), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(3, 0), new DiscreteCoordinates(4,0));

        shopAssistant = new ShopAssistant(this,Orientation.RIGHT, new DiscreteCoordinates(2,5));
        ARPGSign sign = new ARPGSign("--- Welcome to the Chj�p shop --- ---      Offer of the day      ---     10 bombs  for 100 coins !",this,Orientation.DOWN,new DiscreteCoordinates(1,3));

        registerActors(door1, shopAssistant,sign, new Background(this), new Foreground(this));
        //Calling registerActors method in ARPGArea
    }

    public void update(float deltaTime) {
       /* if (!this.exists(shopAssistant)) {
            registerActor(new ShopAssistant(this, Orientation.RIGHT, new DiscreteCoordinates(2, 5)));

        }*/
        super.update(deltaTime);
    }
}

