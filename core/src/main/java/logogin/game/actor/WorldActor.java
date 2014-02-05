package logogin.game.actor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * WorldActor.java
 *
 * @author Pavel Danchenko
 * @date Feb 3, 2014
 *
 */
public class WorldActor extends Actor {

    private final Rectangle rectangle;
    private final Rectangle worldRectangle;

    public WorldActor() {
        rectangle = new Rectangle();
        worldRectangle = new Rectangle();
    }

    public void updateRectangle() {
        rectangle.set(getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Vector2 getPosition() {
        return rectangle.getPosition(new Vector2());
    }

    public void setWorldRectangle(Rectangle rect) {
        worldRectangle.set(rect);
    }

    public void setWorldPosition(Vector2 worldPos) {
        worldRectangle.setPosition(worldPos);
    }
    public Vector2 getWorldPosition() {
        return worldRectangle.getPosition(new Vector2());
    }

    public void setWorldSize(Vector2 worldSize) {
        worldRectangle.setSize(worldSize.x, worldSize.y);
    }
    public Vector2 getWorldSize() {
        return worldRectangle.getSize(new Vector2());
    }

    public void fitWorldSize(float maxWorldSize) {
        Rectangle fitRect = new Rectangle(worldRectangle);
        fitRect.setSize(maxWorldSize);
        Rectangle worldRect = new Rectangle(rectangle);
        worldRect.fitInside(fitRect);

        worldRectangle.setSize(worldRect.getWidth(), worldRect.getHeight());
    }
}