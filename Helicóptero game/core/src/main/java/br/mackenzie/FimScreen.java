package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;

public class FimScreen extends ScreenAdapter {

    private final Main game;

    public FimScreen(Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        game.prepararCamera();
        game.batch.begin();

        game.batch.draw(game.background, 0, 0, Main.LARGURA, Main.ALTURA);
        game.escurecerTela(0.6f);

        game.escreverCentralizado("PARABENS!", 330f, 2.8f, Color.YELLOW);
        game.escreverCentralizado("Voce completou todas as fases", 260f, 1.6f);
        game.escreverCentralizado("Aperte ENTER para voltar ao menu", 170f, 1.4f);

        game.batch.end();
    }
}