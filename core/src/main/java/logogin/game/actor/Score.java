package logogin.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Timer.java
 *
 * @author Pavel Danchenko
 * @date Jan 20, 2014
 *
 */
public class Score extends Actor {

    private int maxValue;
    private int value;

    private BitmapFont font;
    private Texture icon;

    private float iconOffset;

    public Score(int value, int maxValue) {
        this.value = value;
        this.maxValue = maxValue;

        font = new BitmapFont(Gdx.files.internal("data/font/amazdoomleft_regular_64.fnt"));
        icon = new Texture("data/ui/key.png");

        TextBounds bounds = font.getBounds("00/00");
        setWidth(bounds.width + icon.getWidth());
        setHeight(Math.max(icon.getHeight(), bounds.height));
        iconOffset = bounds.width;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        font.draw(batch, formatScore(), getX(), getY() + 50);
        batch.draw(icon, getX() + iconOffset, getY());
    }

    private String formatScore() {
        return String.format("%02d/%02d", value, maxValue);
    }

    public void increment() {
        value++;
    }

    public int getScore() {
        return value;
    }

    public int getMaxScore() {
        return maxValue;
    }

    public boolean isMax() {
        return value == maxValue;
    }
}
