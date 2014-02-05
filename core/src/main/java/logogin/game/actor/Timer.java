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
public class Timer extends Actor {

    private float currentValue;
    private float totalTime;
    private boolean paused;

    private BitmapFont font;
    private Texture hourGlass;

    private float hourglassOffset;

    public Timer(float initialValue) {
        this.totalTime = 0;
        this.currentValue = initialValue;
        this.paused = true;

        font = new BitmapFont(Gdx.files.internal("data/font/amazdoomleft_regular_64.fnt"));
        hourGlass = new Texture("data/ui/hourglass.png");

        TextBounds bounds = font.getBounds("00:00");
        setWidth(bounds.width + hourGlass.getWidth());
        setHeight(Math.max(hourGlass.getHeight(), bounds.height));
        hourglassOffset = bounds.width;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        if ( !paused && currentValue > 0) {
            currentValue -= Gdx.graphics.getDeltaTime();
            if ( currentValue < 0 ) {
                currentValue = 0;
            }
            totalTime += Gdx.graphics.getDeltaTime();
        }

        font.draw(batch, formatTime(), getX(), getY() + 50);
        batch.draw(hourGlass, getX() + hourglassOffset, getY());
    }

    public void pause() {
        paused = true;
    }
    public void start() {
        paused = false;
    }

    public void addTime() {
        currentValue += 10;
    }

    private String formatTime() {
        int mins = (int)currentValue / 60;
        int secs = (int)currentValue - mins * 60;
        return String.format("%02d:%02d", mins, secs);
    }

    public float getTotalTime() {
        return totalTime;
    }

    public boolean isTimeOut() {
        return currentValue <= 0;
    }
}
