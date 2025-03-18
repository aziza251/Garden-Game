package si.um.feri.urinboeva;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen extends ScreenAdapter {

    private final MainGame game;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private Texture background;

    public MenuScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize viewport and stage
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, game.getBatch());

        // Load skin and background
        skin = new Skin(Gdx.files.internal("assets/skin/cloud-form-ui.json"));
        background = new Texture("assets/img_3.png");

        // Add UI components to the stage
        stage.addActor(createUi());

        // Set up InputMultiplexer for stage and keyboard input
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    game.setScreen(new GameScreen(game));
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        background.dispose();
    }

    private Actor createUi() {
        // Create the main table
        Table table = new Table();
        table.defaults().pad(20);

        // Set the background and ensure it doesn't block input
        table.setBackground(new TextureRegionDrawable(background));
        table.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);


        // Create buttons
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Play button clicked!");
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Quit button clicked!");
                Gdx.app.exit();
            }
        });

        // Create a table for buttons
        Table buttonTable = new Table();
        buttonTable.defaults()
            .width(400)
            .height(100)
            .padLeft(30)
            .padRight(30);

        buttonTable.add(playButton).padBottom(15).fillX().row();
        buttonTable.add(quitButton).fillX();
        buttonTable.center();

        // Add the button table to the main table
        table.add(buttonTable);
        table.center();
        table.setFillParent(true);

        return table;
    }
}
