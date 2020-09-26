package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.ARPGSign;
import ch.epfl.cs107.play.game.arpg.actor.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.Assistant;
import ch.epfl.cs107.play.game.arpg.actor.Bicycle;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.Cherry;
import ch.epfl.cs107.play.game.arpg.actor.CherryTree;
import ch.epfl.cs107.play.game.arpg.actor.Chest;
import ch.epfl.cs107.play.game.arpg.actor.Coin;
import ch.epfl.cs107.play.game.arpg.actor.Computer;
import ch.epfl.cs107.play.game.arpg.actor.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.arpg.actor.Heart;
import ch.epfl.cs107.play.game.arpg.actor.Jar;
import ch.epfl.cs107.play.game.arpg.actor.King;
import ch.epfl.cs107.play.game.arpg.actor.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.actor.Mew;
import ch.epfl.cs107.play.game.arpg.actor.NPCCenter;
import ch.epfl.cs107.play.game.arpg.actor.NPCQuest;
import ch.epfl.cs107.play.game.arpg.actor.Orb;
import ch.epfl.cs107.play.game.arpg.actor.Policeman;
import ch.epfl.cs107.play.game.arpg.actor.PressurePlate;
import ch.epfl.cs107.play.game.arpg.actor.Rock;
import ch.epfl.cs107.play.game.arpg.actor.ShopAssistant;
import ch.epfl.cs107.play.game.arpg.actor.Staff;
import ch.epfl.cs107.play.game.arpg.actor.Torch;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.game.rpg.actor.EnvironmentEntity;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {

    /**
     * Simulate an interaction between RPG Interactor and a player
     *
     * @param player (ARPGPlayer), not null
     */
    default void interactWith(ARPGPlayer player) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a cell
     *
     * @param cell (ARPGCell), not null
     */
    default void interactWith(ARPGBehavior.ARPGCell cell) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a grass tile
     *
     * @param grass (Grass), not null
     */
    default void interactWith(Grass grass) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a bomb
     *
     * @param bomb (Bomb), not null
     */
    default void interactWith(Bomb bomb) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and an environment entity
     *
     * @param deco (EnvironmentEntity), not null
     */
    default void interactWith(EnvironmentEntity deco) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a vulnerable actor
     *
     * @param vulnerableActor (VulnerableActor), not null
     */
    default void interactWith(VulnerableActor vulnerableActor) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and an... assistant?
     *
     * @param assistant (Assistant), not null
     */
    default void interactWith(Assistant assistant) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and an NPC
     *
     * @param npc (NPCCenter), not null
     */
    default void interactWith(NPCCenter npc) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a rock
     *
     * @param rock (Rock), not null
     */
    default void interactWith(Rock rock) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a coin
     *
     * @param coin (Coin), not null
     */
    default void interactWith(Coin coin) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a heart
     *
     * @param heart (Heart), not null
     */
    default void interactWith(Heart heart) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a cherry
     *
     * @param cherry (Cherry), not null
     */
    default void interactWith(Cherry cherry) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a castlekey
     *
     * @param castle (CastleKey), not null
     */
    default void interactWith(CastleKey castle) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a castledoor
     *
     * @param castle (CastleDoor), not null
     */
    default void interactWith(CastleDoor castle) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a cherryTree
     *
     * @param cherry (Cherry), not null
     */
    default void interactWith(CherryTree cherry) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a bicycle
     *
     * @param bicycle (Bicycle), not null
     */
    default void interactWith(Bicycle bicycle) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a pressure plate
     *
     * @param press (PressurePlate), not null
     */
    default void interactWith(PressurePlate press) {
        // by default the interaction is empty
    }

    default void interactWith(Jar jar) {
        // by default the interaction is empty
    }

    default void interactWith(Chest chest) {
        // by default the interaction is empty
    }

    default void interactWith(Arrow arrow) {
        // by default the interaction is empty
    }

    default void interactWith(MagicWaterProjectile proj) {
        // by default the interaction is empty
    }

    default void interactWith(King king) {
        // by default the interaction is empty
    }

    default void interactWith(ShopAssistant seller) {
        // by default the interaction is empty
    }

    default void interactWith(Torch torch) {
        // by default the interaction is empty
    }

    default void interactWith(Mew mew) {
        // by default the interaction is empty
    }

    default void interactWith(NPCQuest quest) {
        // by default the interaction is empty
    }

    default void interactWith(FireSpell fire) {
        // by default the interaction is empty
    }

    default void interactWith(CaveDoor caveDoor) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a sign
     *
     * @param sign (ARPGSign), not null
     */
    default void interactWith(ARPGSign sign) {
        // by default the interaction is empty
    }

    /**
     * Simulate an interaction between RPG Interactor and a computer
     *
     * @param computer (Computer), not null
     */
    default void interactWith(Computer computer) {
        // by default the interaction is empty
    }

    default void interactWith(Policeman policeman) {
        // by default the interaction is empty
    }

    default void interactWith(Orb orb) {
       // by default the interaction is empty
    }

    default void interactWith(Staff staff) {
      // by default the interaction is empty
    }

    default void interactWith(Orb.Bridge bridge) {
     // by default the interaction is empty
    }
}
