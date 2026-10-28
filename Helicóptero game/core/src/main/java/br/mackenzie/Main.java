package br.mackenzie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Main extends Game {

    public static final float LARGURA = 800f;
    public static final float ALTURA = 480f;

    public static final float ALTURA_DO_CHAO = 55f;

    public OrthographicCamera camera;
    public FitViewport viewport;
    public SpriteBatch batch;
    public BitmapFont fonte;
    public SensorPressao sensor;

    public Music musicaAmbiente;
    public Music somHelicoptero;
    public Sound somColisao;

    public Texture background;
    private Texture pixel;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA, ALTURA, camera);
        batch = new SpriteBatch();
        fonte = new BitmapFont();

        background = new Texture("background.png");

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();

        sensor = new SensorArduino("COM3");

        setScreen(new MenuScreen(this));

        musicaAmbiente = Gdx.audio.newMusic(Gdx.files.internal("ambiente.mp3"));

        musicaAmbiente.setLooping(true);
        musicaAmbiente.setVolume(0.43f);
        musicaAmbiente.play();

        somHelicoptero = Gdx.audio.newMusic(Gdx.files.internal("helicoptero.mp3"));

        somHelicoptero.setLooping(true);
        somHelicoptero.setVolume(0.12f);

        somColisao = Gdx.audio.newSound(Gdx.files.internal("colisao.mp3"));
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        super.resize(width, height);
    }

    public void prepararCamera() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void escurecerTela(float opacidade) {
        batch.setColor(0f, 0f, 0f, opacidade);
        batch.draw(pixel, 0, 0, LARGURA, ALTURA);
        batch.setColor(Color.WHITE);
    }

    public void escrever(String texto, float x, float y, float escala, Color cor) {
        fonte.getData().setScale(escala);
        fonte.setColor(cor);
        fonte.draw(batch, texto, x, y);
    }

    public void escreverCentralizado(String texto, float y, float escala) {
        escreverCentralizado(texto, y, escala, Color.WHITE);
    }

    public void escreverCentralizado(String texto, float y, float escala, Color cor) {
        fonte.getData().setScale(escala);
        fonte.setColor(cor);
        fonte.draw(batch, texto, 0, y, LARGURA, Align.center, false);
    }

    @Override
    public void dispose() {

        if (sensor instanceof SensorArduino) {
            ((SensorArduino) sensor).fechar();
        }

        if (musicaAmbiente != null) {
            musicaAmbiente.stop();
            musicaAmbiente.dispose();
        }

        if (somHelicoptero != null) {
            somHelicoptero.stop();
            somHelicoptero.dispose();
        }

        if (somColisao != null) {
            somColisao.dispose();
        }

        super.dispose();
        batch.dispose();
        fonte.dispose();
        background.dispose();
        pixel.dispose();
    }
}