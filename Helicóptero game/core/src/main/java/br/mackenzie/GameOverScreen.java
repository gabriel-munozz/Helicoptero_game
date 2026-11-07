package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;

public class GameOverScreen extends ScreenAdapter {

    private final Main game;
    private final Fase fase;
    private final Partida partida;

    public GameOverScreen(Main game, Fase fase, Partida partida) {
        this.game = game;
        this.fase = fase;
        this.partida = partida;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game, fase, partida));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        game.desenharFundoEscurecido(0.65f);

        game.escreverCentralizado("GAME OVER", 340f, 3.2f, Color.RED);
        game.escreverCentralizado(fase.nome, 270f, 1.6f);
        game.escreverCentralizado("ENTER = tentar de novo", 200f, 1.5f);
        game.escreverCentralizado("ESC = menu inicial", 165f, 1.5f);

        game.batch.end();
    }
}