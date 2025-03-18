package si.um.feri.urinboeva;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

public class GameScreen extends ScreenAdapter {


    public SpriteBatch batch;

    private final MainGame game;
    private Texture flower_pink;
    private Texture leaf;
    private Texture rocketImg;
    private Texture garden;
    private Sound crashSound;
    private Sound lossSound;
    private BitmapFont font;

    private Rectangle bird;
    private Array<Rectangle> flowers;
    private Array<Rectangle> leaves;

    private int currentShield = 5;
    private float asteroidSpawnTimer = 0f;
    private float leafSpawnTimer = 5f;

    private static final float ROCKET_SPEED = 400f;
    private static final float ASTEROID_SPAWN_INTERVAL = 1f;
    private static  float ASTEROID_SPEED = 300f;
    private static  float LEAF_SPEED = 400f;
    private static final float LEAF_SPAWN_INTERVAL = 6.5f;

    private int count = 0;

    // Constructor to receive the main game instance
    public GameScreen(MainGame game) {
        this.game = game;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        flower_pink = new Texture("assets/Flower.png");
        garden = new Texture("assets/gardenn.jpg");
        leaf = new Texture("assets/cactus_tall_NE.png");
        rocketImg = new Texture("assets/parrot.png");
        crashSound = Gdx.audio.newSound(Gdx.files.internal("assets/sound.wav"));
        lossSound = Gdx.audio.newSound(Gdx.files.internal("assets/assets_sounds_Swoosh.mp3"));

        font = new BitmapFont();

        flowers = new Array<>();
        leaves = new Array<>();
        createRocket();
    }

    private void createRocket() {
        bird = new Rectangle();
        bird.x = Gdx.graphics.getWidth() / 2f - rocketImg.getWidth() / 2f;
        bird.y = 20f;
        bird.width = rocketImg.getWidth() * 0.75f;
        bird.height = rocketImg.getHeight() * 0.75f;
        System.out.println("Bird bounds: " + bird);


    }

    private void spawnAsteroid() {
        Rectangle asteroid = new Rectangle();
        asteroid.x = MathUtils.random(0f, Gdx.graphics.getWidth() - flower_pink.getWidth());
        asteroid.y = Gdx.graphics.getHeight();
        asteroid.width = flower_pink.getWidth();
        asteroid.height = flower_pink.getHeight();
        flowers.add(asteroid);
    }

    private void spawnLeaf() {
        Rectangle newLeaf = new Rectangle();
        newLeaf.x = MathUtils.random(1f, Gdx.graphics.getWidth() - leaf.getWidth());
        newLeaf.y = Gdx.graphics.getHeight();

        // Adjust the scale factor to make the leaves larger
        newLeaf.width = leaf.getWidth() * 0.75f;  // Adjust the scale factor (50% of original size)
        newLeaf.height = leaf.getHeight() * 0.75f;  // Adjust the scale factor (50% of original size)

        leaves.add(newLeaf);
    }



    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        handleInput(delta);
        update(delta);

        batch.begin();
        batch.draw(garden, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println(" Transitioning to MenuScreen...");
            game.setScreen(new MenuScreen(game));  // Transition to MenuScreen
            return;
        }
        if (currentShield == 0) {
            // If current shield is 0, show Game Over screen
            renderGameOver();


        } else {
            font.getData().setScale(3, 3);
            font.setColor(1, 0, 0, 1);
            font.draw(batch, "Rewards Collected: " + count, 10.0F, Gdx.graphics.getHeight() - 10.0F);
            font.draw(batch, "Lives: " + currentShield, 1300.0F, Gdx.graphics.getHeight() - 10.0F);
            draw();

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                System.out.println(" Transitioning to MenuScreen...");
                game.setScreen(new MenuScreen(game));  // Transition to MenuScreen
                return;
            }
        }
        batch.end();
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (currentShield > 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                bird.x -= ROCKET_SPEED * delta;
                bird.x = Math.max(bird.x, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                bird.x += ROCKET_SPEED * delta;
                bird.x = Math.min(bird.x, Gdx.graphics.getWidth() - bird.width);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                bird.y += ROCKET_SPEED * delta;
                bird.y = Math.min(bird.y, Gdx.graphics.getHeight() - bird.height);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                bird.y -= ROCKET_SPEED * delta;
                bird.y = Math.max(bird.y, 0);
            }
        }
    }

    private void update(float delta) {
        if (currentShield > 0 ) {
            asteroidSpawnTimer += delta;
            leafSpawnTimer += delta;


            if (asteroidSpawnTimer > ASTEROID_SPAWN_INTERVAL) {
                spawnAsteroid();
                asteroidSpawnTimer = 0f;
            }
            if (leafSpawnTimer > LEAF_SPAWN_INTERVAL) {
                spawnLeaf();
                leafSpawnTimer = 1f;
            }
        }




        Iterator<Rectangle> asteroidIterator = flowers.iterator();
        while (asteroidIterator.hasNext()) {
            Rectangle asteroid = asteroidIterator.next();
            asteroid.y -= ASTEROID_SPEED * delta;
            if (asteroid.overlaps(bird)) {
                count++;
                asteroidIterator.remove();
                crashSound.play();
            }
            if (asteroid.y + asteroid.height < 0f) {
                asteroidIterator.remove();
            }
        }

        Iterator<Rectangle> leafIterator = leaves.iterator();
        while (leafIterator.hasNext()) {
            Rectangle leaf = leafIterator.next();
            leaf.y -= LEAF_SPEED * delta;
            if (leaf.overlaps(bird)) {
                System.out.println("Collision Detected:");
                System.out.println("Bird bounds: " + bird);
                System.out.println("Leaf bounds: " + leaf);
                currentShield--;
                leafIterator.remove();
                lossSound.play();
            }
            if (leaf.y + leaf.height < 0f) {
                leafIterator.remove();
            }
        }
    }

    private void renderGameOver() {
        font.getData().setScale(5, 5);
        font.draw(batch, "Game Over", 570.0F, Gdx.graphics.getHeight() - 400.0F);
        font.draw(batch, "Rewards Collected: " + count, 470.0F, Gdx.graphics.getHeight() - 500.0F);
    }

    private void draw() {
        batch.draw(rocketImg, bird.x, bird.y, bird.width, bird.height);
        for (Rectangle asteroid : flowers) {
            batch.draw(flower_pink, asteroid.x, asteroid.y, asteroid.width * 0.3f, asteroid.height * 0.3f);
        }
        for (Rectangle leafItem : leaves) {
            batch.draw(leaf, leafItem.x, leafItem.y, leafItem.width * 1.3f, leafItem.height * 1.3f);
            System.out.println(leafItem);
        }
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        flower_pink.dispose();
        leaf.dispose();
        rocketImg.dispose();
        crashSound.dispose();
        lossSound.dispose();
        font.dispose();
        garden.dispose();
    }


}
