package logogin.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Mimic.java
 *
 * @author Pavel Danchenko
 * @date Dec 17, 2013
 *
 */
public class Mimic extends MovingActor {

	private Animation walkAnimation;
	private TextureRegion currentFrame;

	private float stateTime;
    private float duration;

    public Mimic() {
        TextureRegion[] walkFrames = AnimationHelper.createFrames("data/world/mimic_hopping.png", 7, 1);
		walkAnimation = new Animation(0.05f, walkFrames);
		stateTime = 0f;

        setWidth(walkFrames[0].getRegionWidth());
        setHeight(walkFrames[0].getRegionHeight());
    }

    public Vector2 decideDirection() {
        Vector2 dir = getDirection().cpy();
        if ( duration <= 0 ) {
            // may be idle
            if ( MathUtils.randomBoolean(0.1f) ) {
                dir = Vector2.Zero.cpy();
            } else {
                float len = dir.len();
                if ( len < 2 ) {
                    dir = Vector2.X.cpy().scl(MathUtils.random(2f, 3f));
                }
                float angle = MathUtils.random(360f);
                dir.setAngle(angle);
            }
            duration = MathUtils.random(3, 5);
        }
        return dir;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
    	float delta = Gdx.graphics.getDeltaTime();
        duration -= delta;
        if ( !getDirection().equals(Vector2.Zero) ) {
    	    stateTime += delta;
    	}
    	currentFrame = walkAnimation.getKeyFrame(stateTime, true);

    	if ( getDirection().x > 0 && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        if ( getDirection().x < 0 && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

    	batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    	updateRectangle();
    }
}
