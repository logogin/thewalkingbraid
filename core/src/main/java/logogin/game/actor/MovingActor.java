package logogin.game.actor;

import com.badlogic.gdx.math.Vector2;

/**
 * MovingActor.java
 *
 * @author Pavel Danchenko
 * @date Feb 4, 2014
 *
 */
public class MovingActor extends WorldActor {

    private Vector2 direction;

    public MovingActor() {
        super();
        direction = Vector2.Zero.cpy();
    }

    public void move(Vector2 worldDist) {
        Vector2 newPos = getWorldPosition().add(worldDist);
        setWorldPosition(newPos);
        direction.set(worldDist);
    }

    public Vector2 getDirection() {
        return direction;
    }
}
