package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;

public class MenuScreen extends ScreenAdapter {

    private final Main game;
    private int selecionada = 0;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        int total = Fase.TODAS.length;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selecionada = (selecionada - 1 + total) % total;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selecionada = (selecionada + 1) % total;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game, Fase.TODAS[selecionada]));
            return;
        }

        game.prepararCamera();
        game.batch.begin();

        game.batch.draw(game.background, 0, 0, Main.LARGURA, Main.ALTURA);
        game.escurecerTela(0.5f);

        game.escreverCentralizado("RESGATE DE HELICOPTERO", 420f, 2.4f);
        game.escreverCentralizado("Escolha a fase", 340f, 1.5f);

        for (int i = 0; i < total; i++) {
            float y = 285f - i * 45f;
            if (i == selecionada) {
                game.escreverCentralizado("> " + Fase.TODAS[i].nome + " <", y, 1.9f, Color.YELLOW);
            } else {
                game.escreverCentralizado(Fase.TODAS[i].nome, y, 1.6f);
            }
        }

        game.escreverCentralizado("ENTER: jogar", 120f, 1.3f);
        game.escreverCentralizado("Durante o jogo:  P = pausa   R = reinicia   ESC = menu", 60f, 1.2f);

        game.batch.end();
    }
}