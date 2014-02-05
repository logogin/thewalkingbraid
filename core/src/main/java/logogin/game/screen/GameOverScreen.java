package logogin.game.screen;

import logogin.game.actor.Score;
import logogin.game.actor.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * GameOverScreen.java
 *
 * @author Pavel Danchenko
 * @date Feb 3, 2014
 *
 */
public class GameOverScreen extends AbstractScreen {

    private BitmapFont font;
    private Timer timer;
    private Score score;

    private Stage stage;
    private SpriteBatch batch;

    public GameOverScreen(float totalTime, int totalScore, int maxScore) {
        font = new BitmapFont(Gdx.files.internal("data/font/amazdoomleft_regular_64.fnt"));
        batch = new SpriteBatch();
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);

        createUI(totalTime, totalScore, maxScore);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.begin();
        font.setColor(Color.GREEN);
        font.draw(batch, "GAME OVER", Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight()/2 + timer.getHeight());
        batch.end();
    }

    private void createUI(float totalTime, int totalScore, int maxScore) {
        timer = new Timer(totalTime);
        stage.addActor(timer);
        timer.setPosition(Gdx.graphics.getWidth()/2 - timer.getWidth() - 10, Gdx.graphics.getHeight()/2 - timer.getHeight());

        score = new Score(totalScore,  maxScore);
        stage.addActor(score);
        score.setPosition(Gdx.graphics.getWidth()/2 + 10, Gdx.graphics.getHeight()/2 - score.getHeight() - 20);
    }
}
