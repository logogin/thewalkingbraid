package logogin.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Player.java
 *
 * @author Pavel Danchenko
 * @date Dec 17, 2013
 *
 */
public class Player extends MovingActor {

	private Animation runAnimation;
	private Animation idleAnimation;
	private TextureRegion[] runFrames;
    private TextureRegion[] idleFrames;
	private TextureRegion currentFrame;
	private float stateTime;

    public enum State {
        RUNNING
        , IDLE;
    }

    private float shockTime;
    private State state;

    public Player() {
        runFrames = AnimationHelper.createFrames("data/world/player_running.png", 27, 1);
        idleFrames = AnimationHelper.createFrames("data/world/player_idle.png", 22, 1);
		runAnimation = new Animation(0.025f, runFrames);
		idleAnimation = new Animation(0.1f, idleFrames);
		stateTime = 0f;

		state = State.IDLE;

		setWidth(idleFrames[0].getRegionWidth());
        setHeight(idleFrames[0].getRegionHeight());
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        float delta = Gdx.graphics.getDeltaTime();
        if ( isShocked() ) {
            shockTime -= delta;
        } else {
            shockTime = 0;
        }
    	if ( !getDirection().equals(Vector2.Zero) ) {
    	    if ( state == State.IDLE ) {
    	        stateTime = 0f;
    	        state = State.RUNNING;
    	        setWidth(runFrames[0].getRegionWidth());
    	        setHeight(runFrames[0].getRegionHeight());
    	        updateRectangle();
    	        fire(new ChangeListener.ChangeEvent());
    	    }
    	    stateTime += delta;
    	    currentFrame = runAnimation.getKeyFrame(stateTime, true);
    	} else {
    	    if ( state == State.RUNNING ) {
    	        stateTime = 0f;
    	        state = State.IDLE;
    	        setWidth(idleFrames[0].getRegionWidth());
                setHeight(idleFrames[0].getRegionHeight());
                updateRectangle();
    	        fire(new ChangeListener.ChangeEvent());
    	    }
    	    stateTime += delta;
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
    	}

    	if ( getDirection().x < 0 && !currentFrame.isFlipX()) {
    	    currentFrame.flip(true, false);
    	}

    	if ( getDirection().x > 0 && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

	    batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

	    updateRectangle();
    }

    public void makeShocked() {
        if ( !isShocked() ) {
            shockTime += 5;
        }
    }

    public boolean isShocked() {
        return shockTime > 0;
    }

    public float getShockTime() {
        return shockTime;
    }
}
