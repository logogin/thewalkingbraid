package logogin.game.screen;

import java.util.ArrayList;
import java.util.List;

import logogin.game.TheWalkingBraid;
import logogin.game.actor.Key;
import logogin.game.actor.Mimic;
import logogin.game.actor.Player;
import logogin.game.actor.Score;
import logogin.game.actor.Timer;
import logogin.game.actor.WorldActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * GameScreen.java
 *
 * @author Pavel Danchenko
 * @date Dec 17, 2013
 *
 */
public class GameScreen extends AbstractScreen {

    private TheWalkingBraid parentGame;

	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
    private Stage stage;
    private SpriteBatch batch;
    private Touchpad touchpad;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private BitmapFont font;

    private Timer timer;
    private Score score;
    private Player player;
    private final List<Key> keys = new ArrayList<Key>();
    private final List<Mimic> mimics = new ArrayList<Mimic>();
    private TiledMapTileLayer walls;

    private float playerSpeedFactor = 5;

    public static final float UNIT = 24;

    public GameScreen(TheWalkingBraid parentGame) {
        this.parentGame = parentGame;

        font = new BitmapFont();
        batch = new SpriteBatch();

        //Create a Stage and add TouchPad
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);

        // load the map, set the unit scale to 1/24 (1 unit == 24 pixels)
        TiledMap map = new TmxMapLoader().load("data/map/level.tmx");
        MapLayer objects = map.getLayers().get("Objects");
        walls = (TiledMapTileLayer)map.getLayers().get("Walls");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / UNIT);

        //initial position
        createCamera();
        createActors(objects.getObjects());
        createUI();

        updateCamera();

        setActorsBounds();
    }

    private void triggerGameOver() {
        parentGame.triggerGameOver(timer.getTotalTime(), score.getScore(), score.getMaxScore());
    }

    private void createUI() {
        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("data/ui/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("data/ui/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);

        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);

        timer = new Timer(60);
        stage.addActor(timer);
        timer.setPosition(10, Gdx.graphics.getHeight() - timer.getHeight());

        score = new Score(0,  keys.size());
        stage.addActor(score);
        score.setPosition(Gdx.graphics.getWidth() - score.getWidth() - 10, Gdx.graphics.getHeight() - score.getHeight() - 20);
    }

	private void setActorsBounds() {
        setActorBounds(player);
        for (Mimic zombie : mimics) {
            setActorBounds(zombie);
        }
        for(Key key : keys) {
            setActorBounds(key);
        }
    }

    private void setActorBounds(WorldActor actor) {
        //num of cells to fit
        int cells = 2;
        //must fit 80% of cells area
        float maxSize = cells * UNIT * 0.8f;
        actor.fitWorldSize(maxSize);

        Vector2 scrOrigin = world2Screen(actor.getWorldPosition());
        Vector2 scrTop = world2Screen(actor.getWorldPosition().add(actor.getWorldSize()));
        actor.setBounds(scrOrigin.x, scrOrigin.y, scrTop.x - scrOrigin.x, scrTop.y - scrOrigin.y);
        actor.updateRectangle();
    }

    @Override
    public void show() {
        timer.start();
    }

    @Override
    public void render(float delta) {
        if ( score.isMax() || timer.isTimeOut()) {
            triggerGameOver();
        }

        Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        updateCamera();

        // set the tile map rendere view based on what the
        // camera sees and render the map
        renderer.setView(camera);
        renderer.render();

    	updatePlayer();
        updateMimics();
    	updateCoins();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        drawDebug();
    }

    private void drawDebug() {
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        font.draw(batch, String.format("Player: world%s screen%s direction%s"
                , player.getWorldPosition(), player.getPosition(), player.getDirection()), 10, 40);
        font.draw(batch, "Camera: " + camera.position, 10, 60);
        font.draw(batch, String.format("ShockTime: %f totalTime %f", player.getShockTime(), timer.getTotalTime()), 10, 80);
        batch.end();
    }

    private void updatePlayer() {
        if ( player.isShocked() ) {
            playerSpeedFactor = 2;
        } else {
            playerSpeedFactor = 5;
        }

        Vector2 dist = new Vector2(touchpad.getKnobPercentX() * playerSpeedFactor, touchpad.getKnobPercentY() * playerSpeedFactor);

        dist = moveToFlexible(player, dist, walls);
        player.move(dist);
        updateScreenPosition(player);
    }

    private void updateMimics() {
        for ( Mimic mimic : mimics ) {
            if ( mimic.getRectangle().overlaps(player.getRectangle()) ) {
                player.makeShocked();
            } else {
                Vector2 dist = mimic.decideDirection();
                dist = moveToFlexible(mimic, dist, walls);
                mimic.move(dist);
            }
            updateScreenPosition(mimic);
        }
    }

    private void updateCoins() {
        for ( Key key : keys ) {
            if( key.isVisible() ) {
                if ( player.getRectangle().overlaps(key.getRectangle()) ) {
                    key.setVisible(false);
                    score.increment();
                    timer.addTime();

                } else {
                    Vector2 scrPos = world2Screen(key.getWorldPosition());
                    key.setPosition(scrPos.x, scrPos.y);
                }
                updateScreenPosition(key);
            }
        }
    }

    private void updateCamera() {
        camera.position.set(world2Camera(player.getWorldPosition()), 0);
        camera.update();
    }

    private void updateScreenPosition(WorldActor actor) {
        Vector2 scrPos = world2Screen(actor.getWorldPosition());
        actor.setPosition(scrPos.x, scrPos.y);
        actor.updateRectangle();
    }

    private void createActors(MapObjects objects) {
        for (MapObject object : objects) {
            if ( object.getName().startsWith("Mimic") ) {
                createMimic(((RectangleMapObject)object).getRectangle());
                continue;
            }
            if ( object.getName().startsWith("Key") ) {
                createKey(((RectangleMapObject)object).getRectangle());
                continue;
            }
            if ( "Player".equals(object.getName()) ) {
                createPlayer(((RectangleMapObject)object).getRectangle());
                continue;
            }
        }
    }

    private void createCamera() {
        //Create camera
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        // create an orthographic camera, shows us 15x15 units of the world
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 15 * aspectRatio, 15);
    }

    private void createPlayer(Rectangle rect) {
        player = new Player();
        player.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setActorBounds((WorldActor)actor);
            }
        });
        player.setWorldRectangle(rect);
        player.updateRectangle();
        stage.addActor(player);
    }

    private void createMimic(Rectangle rect) {
        Mimic mimic = new Mimic();
        mimic.setWorldRectangle(rect);
        mimic.updateRectangle();
        mimics.add(mimic);
        stage.addActor(mimic);
    }

    private void createKey(Rectangle rect) {
        Key key = new Key();
        key.setWorldRectangle(rect);
        key.updateRectangle();
        keys.add(key);
        stage.addActor(key);
    }

    private Vector2 world2Screen(Vector2 worldPos) {
        Vector2 vec = world2Camera(worldPos);
        return camera2Screen(vec);
    }

    public Vector2 world2Camera(Vector2 worldPos) {
        return worldPos.cpy().div(UNIT);
    }

    private Vector2 camera2Screen(Vector2 cameraPos) {
        Vector3 vec = new Vector3(cameraPos, 0);
        camera.project(vec);
        return new Vector2(vec.x, vec.y);
    }

    private boolean canMoveTo(WorldActor actor, Vector2 dist, TiledMapTileLayer walls) {
        if ( !isMoving(dist) ) {
            return true;
        }
        Vector2 newPos = world2Camera(actor.getWorldPosition().add(dist));
        Vector2 cameraSize = world2Camera(actor.getWorldSize());
        Rectangle rect = new Rectangle(newPos.x, newPos.y, cameraSize.x, cameraSize.y);
        int startX = (int)rect.x;
        int startY = (int)rect.y;
        int endX = (int)(rect.x + rect.getWidth());
        int endY = (int)(rect.y + rect.getHeight());
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                Cell cell = walls.getCell(i, j);
                if ( null != cell ) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isMoving(Vector2 dist) {
        return dist.x != 0 || dist.y != 0;
    }

    /**
     * Tries to move the actor to the new position, but if it can not move it in both directions, it moves him only in one direction.
     * With this you can slide on walls.
     */
    private Vector2 moveToFlexible(WorldActor actor, Vector2 dist, TiledMapTileLayer walls) {
        //try move to both directions
        if ( !isMoving(dist) || canMoveTo(actor, dist, walls) ) {
            return dist;
        }
        if ( canMoveTo(actor, new Vector2(dist.x, 0), walls) ) {
            return new Vector2(dist.x, 0);
        }
        if ( canMoveTo(actor, new Vector2(0, dist.y), walls) ) {
            return new Vector2(0, dist.y);
        }
        return Vector2.Zero.cpy();
    }
}
