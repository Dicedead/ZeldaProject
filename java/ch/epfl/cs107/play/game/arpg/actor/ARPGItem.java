package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.rpg.RPG.InventoryItem;
import ch.epfl.cs107.play.math.RegionOfInterest;

public enum ARPGItem implements InventoryItem {
    Arrow("Arrow", 0.f, 10, "zelda/arrow.icon",new RegionOfInterest(0, 0, 16, 16)),
    Sword("Sword", 0.f, 200, "zelda/sword.icon",new RegionOfInterest(0, 0, 16, 16)),
    Bow("Bow", 0.f, 500, "zelda/bow.icon",new RegionOfInterest(0, 0, 16, 16)),
    Staff("Staff", 0.f, 2000, "zelda/staff_water.icon",new RegionOfInterest(0, 0, 16, 16)),
    Bomb("Bomb", 0.f, 100, "zelda/bomb",new RegionOfInterest(0, 0, 16, 16)),
    CastleKey("CastleKey", 0.f, 2000, "zelda/key",new RegionOfInterest(0, 0, 16, 16)),
    Cherry("Cherry",0.f,10,"Cherry",new RegionOfInterest(0, 0, 246, 246)),
    Bicycle("Bicycle",0.f,300,"zelda/tricycle",new RegionOfInterest(0, 0, 24, 32)),
    Stick("Stick",0.f,0,"zelda/stick",new RegionOfInterest(0, 0, 16, 16)),
    Shield("Shield",0.f,100,"zelda/shield",new RegionOfInterest(0, -1, 32, 32)),
    Mew("Mew",0.f,1000,"mew.fixed",new RegionOfInterest(0, 0, 16, 16));

    private final String name;
    private final float weight;
    private final int price;
    private final String spriteName;
    private final RegionOfInterest spriteRoi;

    /**
     * ARPGItem constructor
     *
     * @param nameItem     (String) : gives the name of the article
     * @param weightItem    (float) : gives the weight of the article
     * @param priceItem    (int) : gives the price of the item
     * @param spriteString (String) : gives the item's sprite's name
     * @param roi          (RegionOfInterest) : gives the region selected for the sprite
     */
    ARPGItem(String nameItem, float weightItem, int priceItem, String spriteString, RegionOfInterest roi) {
        name = nameItem;
        weight = weightItem;
        price = priceItem;
        spriteName = spriteString;
        spriteRoi = roi;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSprite() {
        return spriteName;
    }

    public RegionOfInterest getRoi() {
        return spriteRoi;
    }

}