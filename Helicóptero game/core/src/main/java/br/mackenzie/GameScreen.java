package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;

public class GameScreen extends ScreenAdapter {

    private final Main game;
    private final Player player;
    private final Vitima vitima;

    private final float velocidadeDoCenario = 150f;
    private float deslocamentoDoFundo = 0f;

    private int resgates = 0;
    private boolean pausado = false;

    public GameScreen(Main game) {
        this.game = game;
        this.player = new Player();
        this.vitima = new Vitima();
    }

    @Override
    public void render(float delta) {
        if (tratarTeclas()) return;

        if (!pausado) {
            atualizar(delta);
        }
        desenhar();
    }

    private boolean tratarTeclas() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game));
            return true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pausado = !pausado;
        }
        return false;
    }

    private void atualizar(float delta) {
        float pressao = game.sensor.ler(delta);
        player.atualizar(pressao, delta);

        deslocamentoDoFundo = (deslocamentoDoFundo + velocidadeDoCenario * delta) % Main.LARGURA;
        vitima.atualizar(velocidadeDoCenario * delta);

        if (player.getArea().overlaps(vitima.getArea())) {
            resgates++;
            vitima.reaparecer();
        }
    }

    private void desenhar() {
        game.prepararCamera();
        game.batch.begin();

        game.batch.draw(game.background, -deslocamentoDoFundo, 0, Main.LARGURA, Main.ALTURA);
        game.batch.draw(game.background, Main.LARGURA - deslocamentoDoFundo, 0, Main.LARGURA, Main.ALTURA);
        vitima.desenhar(game.batch);
        player.desenhar(game.batch);

        if (pausado) {
            game.escurecerTela(0.6f);
            game.escreverCentralizado("PAUSADO", 330f, 2.6f);
            game.escreverCentralizado("P = continuar", 240f, 1.5f);
            game.escreverCentralizado("R = reiniciar", 205f, 1.5f);
            game.escreverCentralizado("ESC = menu inicial", 170f, 1.5f);
        }

        game.batch.end();
    }

    @Override
    public void hide() {
        player.dispose();
        vitima.dispose();
    }
}