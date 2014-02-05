package logogin.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Key extends WorldActor {

    private final Texture icon;

    public Key() {
        icon = new Texture(Gdx.files.internal("data/world/key.png"));

        setWidth(icon.getWidth());
        setHeight(icon.getHeight());
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        batch.draw(icon, getX(), getY());
        updateRectangle();
    }

}
