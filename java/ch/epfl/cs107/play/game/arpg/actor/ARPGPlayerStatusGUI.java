package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerStatusGUI implements Graphics {

    private final static int DEPTH = 1000;

    private float playerHp;
    private int playerMoney;
    private ARPGItem itemToDraw;

    private float width;
    private float height;

    private float horizontalVect;
    private float remainingHp;
    private int currentDigit;
    private String moneyString;
    private int numberOfItems;
    private String numberOfItemString;

    public void setGUIInformation(float hp, int money, ARPGItem item, int quantity) {
        playerHp = hp;
        playerMoney = money;
        itemToDraw = item;
        numberOfItems = quantity;
    }

    @Override
    public void draw(Canvas canvas) {

        width = canvas.getScaledWidth();
        height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new
                Vector(width / 2, height / 2));

        drawGUIComponent("zelda/gearDisplay", 1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0, height - 1.75f)), 1, DEPTH).draw(canvas);
        drawGUIComponent("zelda/gearDisplay", 1.2f, 1.2f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0.5f, height - 2.5f)), 1, DEPTH).draw(canvas);

        for (int i = 0; i < ARPGPlayer.MAX_HP; ++i) {
            horizontalVect = 1.5f + i;
            remainingHp = playerHp - i;
            if (remainingHp >= 1) {
                drawGUIComponent("zelda/heartDisplay", 1f, 1f, new RegionOfInterest(0, 0, 16, 32), anchor.add(new Vector(horizontalVect, height - 1.5f)), 1, DEPTH).draw(canvas);
            } else if (remainingHp >= 0.75f) {
                drawGUIComponent("zelda/heartDisplay", 1f, 1f, new RegionOfInterest(16, 0, 16, 32), anchor.add(new Vector(horizontalVect, height - 1.5f)), 1, DEPTH).draw(canvas);
            } else if (remainingHp >= 0.5f) {
                drawGUIComponent("zelda/heartDisplay", 1f, 1f, new RegionOfInterest(32, 0, 16, 32), anchor.add(new Vector(horizontalVect, height - 1.5f)), 1, DEPTH).draw(canvas);
            } else if (remainingHp >= 0.25f) {
                drawGUIComponent("zelda/heartDisplay", 1f, 1f, new RegionOfInterest(48, 0, 16, 32), anchor.add(new Vector(horizontalVect, height - 1.5f)), 1, DEPTH).draw(canvas);
            } else {
                drawGUIComponent("zelda/heartDisplay", 1f, 1f, new RegionOfInterest(64, 0, 16, 32), anchor.add(new Vector(horizontalVect, height - 1.5f)), 1, DEPTH).draw(canvas);
            }
        }

        drawGUIComponent("zelda/coinsDisplay", 3.5f, 1.75f, new RegionOfInterest(0, 0, 64, 32), anchor, 1, DEPTH).draw(canvas);

        moneyString = String.valueOf(playerMoney);
        for (int i = moneyString.length()-1; i >= 0; --i) {
            horizontalVect = 1.4f + 0.6f * i;
            currentDigit = Integer.parseInt(moneyString.substring(i,i+1));

            drawGUIComponent("zelda/digits", 0.65f, 0.85f, new RegionOfInterest(currentDigit * 16, 0, 16, 32), anchor.add(new Vector(horizontalVect, 0.475f)), 1, DEPTH).draw(canvas);
        }

        numberOfItemString = String.valueOf(numberOfItems);
        for(int i = numberOfItemString.length()-1;i >= 0;--i) {
            horizontalVect = 0.8f + 0.2f* i;
            currentDigit = Integer.parseInt(numberOfItemString.substring(i,i+1));

            drawGUIComponent("zelda/digits",0.45f, 0.65f, new RegionOfInterest(currentDigit*16, 0, 16, 32), anchor.add(new Vector(horizontalVect, height - 2.25f)), 1, DEPTH).draw(canvas);
        }

        drawGUIComponent(itemToDraw.getSprite(),0.8f,0.8f,itemToDraw.getRoi(),anchor.add(new Vector (0.355f,11.048f)),1f,1100).draw(canvas);
    }

    private ImageGraphics drawGUIComponent(String name, float width, float height, RegionOfInterest roi, Vector anchor, float alpha, float depth) {
        return new
                ImageGraphics(ResourcePath.getSprite(name),
                width, height, roi,
                anchor, alpha, depth);
    }
}