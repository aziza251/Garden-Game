package si.um.feri.urinboeva;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainGame extends Game {

    public SpriteBatch batch;
    private ShapeRenderer renderer;


    @Override
    public void create() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        setScreen(new IntroScreen(this)); // Start with IntroScreen
    }



    @Override
    public void dispose() {

        batch.dispose();
        renderer.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getRenderer() {
        return renderer;
    }

}
