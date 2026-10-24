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

    public static final float ALTURA_DO_CHAO = 74f;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;

    private Texture background;
    private Player player;
    private Vitima vitima;
    private SensorPressao sensor;

    private int resgates = 0;

    private float velocidadeDoCenario = 150f;
    private float deslocamentoDoFundo = 0f;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA, ALTURA, camera);
        batch = new SpriteBatch();

        background = new Texture("background.png");
        player = new Player();
        vitima = new Vitima();
        sensor = new SensorSimulado();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        float pressao = sensor.ler(delta);
        player.atualizar(pressao, delta);
        deslocamentoDoFundo = (deslocamentoDoFundo + velocidadeDoCenario * delta) % LARGURA;

        vitima.atualizar(velocidadeDoCenario * delta);

        if (player.getArea().overlaps(vitima.getArea())) {
            resgates++;
            vitima.reaparecer();
        }

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, -deslocamentoDoFundo, 0, LARGURA, ALTURA);
        batch.draw(background, LARGURA - deslocamentoDoFundo, 0, LARGURA, ALTURA);
        vitima.desenhar(batch);
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
        vitima.dispose();
    }
}