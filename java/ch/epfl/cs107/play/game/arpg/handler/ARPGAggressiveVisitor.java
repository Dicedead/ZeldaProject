package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;

/**
 * Defines handlers with aggressive properties
 */
public interface ARPGAggressiveVisitor extends ARPGInteractionVisitor {

    void interactWith(VulnerableActor enemy);
}
