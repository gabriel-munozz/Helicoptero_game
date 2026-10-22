package br.mackenzie;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    public static final float LARGURA = 800f;
    public static final float ALTURA = 480f;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;

    private Texture background;
    private Player player;

    private float velocidadeDoCenario = 150f;

    private float deslocamentoDoFundo = 0f;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA, ALTURA, camera);
        batch = new SpriteBatch();

        background = new Texture("background.png");
        player = new Player();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        deslocamentoDoFundo = (deslocamentoDoFundo + velocidadeDoCenario * delta) % LARGURA;

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(background, -deslocamentoDoFundo, 0, LARGURA, ALTURA);
        batch.draw(background, LARGURA - deslocamentoDoFundo, 0, LARGURA, ALTURA);

        player.desenhar(batch);
        batch.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        player.dispose();
    }
}