package logogin.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * AnimationHelper.java
 *
 * @author Pavel Danchenko
 * @date Jan 16, 2014
 *
 */
public class AnimationHelper {

     public static TextureRegion[] createFrames(String path, int cols, int rows) {
        Texture spriteSheet = new Texture(Gdx.files.internal(path));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() /  cols, spriteSheet.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return frames;
    }

}
