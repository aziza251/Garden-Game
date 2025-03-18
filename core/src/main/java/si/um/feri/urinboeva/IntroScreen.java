package si.um.feri.urinboeva;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class IntroScreen extends ScreenAdapter {

    private static final float INTRO_DURATION = 3f;

    private final MainGame game;
    private final Stage stage;
    private final Texture keyTexture;
    private final Texture keyholeTexture;
    private Sound introsound;
    private long soundId;
    public IntroScreen(MainGame game) {

        this.game = game;
        this.keyTexture = new Texture("assets/img_2.png");

        this.keyholeTexture = new Texture("assets/img.png");

        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), game.batch);
        this.introsound = Gdx.audio.newSound(Gdx.files.internal("assets/starter.mp3"));
    }

    @Override
    public void show() {
        soundId = introsound.play();
        Image keyhole = new Image(keyholeTexture);
        keyhole.setSize(stage.getWidth(), stage.getHeight());
        keyhole.setPosition(0,0);
        stage.addActor(keyhole);
        Image key = new Image(keyTexture);
        key.setSize(stage.getWidth(), stage.getHeight());
        key.setOrigin(Align.center);
        key.setPosition(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight());
        key.addAction(Actions.sequence(
            Actions.parallel(
                Actions.rotateBy(1000, 2.5f),
                Actions.moveTo(keyhole.getX(), keyhole.getY(), 1.5f)
            ),
            Actions.rotateBy(-360, 1),
            Actions.scaleTo(0, 0, 0.5f),
            Actions.run(() -> game.setScreen(new GameScreen(game))) // Transition to GameScreen
        ));

        stage.addActor(keyhole);
        stage.addActor(key);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.25f, 0.5f, 0.8f, 1f);
        stage.act(delta);
        stage.draw();
    }
public void hide(){
        introsound.stop(soundId);
}
    @Override
    public void dispose() {
        stage.dispose();
        keyTexture.dispose();
        keyholeTexture.dispose();
        introsound.dispose();
    }
}
