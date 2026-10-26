package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;

public class MenuScreen extends ScreenAdapter {

    private final Main game;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            return;
        }

        game.prepararCamera();
        game.batch.begin();

        game.batch.draw(game.background, 0, 0, Main.LARGURA, Main.ALTURA);
        game.escurecerTela(0.5f);

        game.escreverCentralizado("RESGATE DE HELICOPTERO", 360f, 2.4f);
        game.escreverCentralizado("Aperte ENTER para jogar", 190f, 1.8f);
        game.escreverCentralizado("Durante o jogo:  P = pausa   R = reinicia   ESC = menu", 110f, 1.2f);

        game.batch.end();
    }
}