package logogin.game;

import logogin.game.screen.GameOverScreen;
import logogin.game.screen.GameScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GLTexture;

/**
 * TheWalkingBraid.java
 *
 * @author Pavel Danchenko
 * @date Dec 17, 2013
 *
 */
public class TheWalkingBraid extends Game {

    @Override
    public void create() {
        GLTexture.setEnforcePotImages(false);
        setScreen(new GameScreen(this));
    }

	public void triggerGameOver(float totalTime, int score, int maxScore) {
	    GameOverScreen gameOverScreen = new GameOverScreen(totalTime, score, maxScore);
	    setScreen(gameOverScreen);
	}
}
