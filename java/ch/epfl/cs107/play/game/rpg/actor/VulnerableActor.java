package ch.epfl.cs107.play.game.rpg.actor;

import ch.epfl.cs107.play.game.actor.Actor;

/**
 * Defines actors vulnerable to some stuff
 */
public interface VulnerableActor extends Actor {

    void loseHp(float deltaHp,Vulnerabilities... attackTypes);

    /**
     * Enum of the 3 basic vulnerabilities
     */
    enum Vulnerabilities {
        PHYSICAL,
        FIRE,
        MAGIC,
        ;
    }
}
