package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Chateau;
import ch.epfl.cs107.play.game.arpg.area.Ferme;
import ch.epfl.cs107.play.game.arpg.area.Grotte;
import ch.epfl.cs107.play.game.arpg.area.GrotteMew;
import ch.epfl.cs107.play.game.arpg.area.MiniMap;
import ch.epfl.cs107.play.game.arpg.area.PetalburgCenter;
import ch.epfl.cs107.play.game.arpg.area.PetalburgShop;
import ch.epfl.cs107.play.game.arpg.area.PetalburgTimmy;
import ch.epfl.cs107.play.game.arpg.area.Pont;
import ch.epfl.cs107.play.game.arpg.area.Route;
import ch.epfl.cs107.play.game.arpg.area.RouteChateau;
import ch.epfl.cs107.play.game.arpg.area.RouteTemple;
import ch.epfl.cs107.play.game.arpg.area.Temple;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {

    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private final String[] areas = {"zelda/Ferme", "zelda/Village", "zelda/Route", "zelda/Chateau", "zelda/RouteChateau",
            "PetalburgTimmy", "Grotte", "GrotteMew","zelda/RouteTemple","zelda/Temple","PetalburgCenter","PetalburgShop","zelda/PontTropLong","zelda/MiniMap"};
    private final DiscreteCoordinates[] startingPositions = {new DiscreteCoordinates(6, 10),
            new DiscreteCoordinates(5, 15), new DiscreteCoordinates(5, 12),
            new DiscreteCoordinates(7, 2), new DiscreteCoordinates(9, 12),
            new DiscreteCoordinates(4, 4), new DiscreteCoordinates(16, 4),
            new DiscreteCoordinates(8, 5), new DiscreteCoordinates(1,4),
            new DiscreteCoordinates(2,3), new DiscreteCoordinates(5,4),
            new DiscreteCoordinates(5,4), new DiscreteCoordinates(9,2),new DiscreteCoordinates(10,10)};

    /**
     * Add all the areas
     */
    private void createAreas() {
        addArea(new Ferme());
        addArea(new Village());
        addArea(new Route());
        addArea(new RouteChateau());
        addArea(new Chateau());
        addArea(new PetalburgTimmy());
        addArea(new Grotte());
        addArea(new GrotteMew());
        addArea(new RouteTemple());
        addArea(new Temple());
        addArea(new PetalburgCenter());
        addArea(new PetalburgShop());
        addArea(new Pont());
        addArea(new MiniMap());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {


        if (super.begin(window, fileSystem)) {

            createAreas();
            int areaIndex = 5;
            Area area = setCurrentArea(areas[areaIndex], true);
            Player player = getPlayer();
            player = new ARPGPlayer(area, Orientation.DOWN, startingPositions[areaIndex]);
            initPlayer(player);
            return true;
        }
        return false;
    }

    @Override
    public String getTitle() {
        return "NullPointerPanini";
    }

}