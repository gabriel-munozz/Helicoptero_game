package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;

public class ResumoFaseScreen extends ScreenAdapter {

    private final Main game;
    private final Fase fase;
    private final Partida partida;
    private final Metricas metricas;

    public ResumoFaseScreen(Main game, Fase fase, Partida partida, Metricas metricas) {
        this.game = game;
        this.fase = fase;
        this.partida = partida;
        this.metricas = metricas;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Fase proxima = fase.getProxima();
            if (proxima != null) {
                game.setScreen(new GameScreen(game, proxima, partida));
            } else {
                game.setScreen(new FimScreen(game, partida));
            }
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        game.prepararCamera();
        game.batch.begin();

        game.batch.draw(game.background, 0, 0, Main.LARGURA, Main.ALTURA);
        game.escurecerTela(0.65f);

        game.escreverCentralizado(fase.nome.toUpperCase() + " CONCLUIDA!", 445f, 2.4f, Color.YELLOW);

        float y = 385f;
        int apareceram = metricas.getResgates() + metricas.getVitimasPerdidas();

        game.escreverCentralizado(String.format("Tempo da fase: %.1f s", metricas.getTempoDaFase()), y, 1.4f);
        game.escreverCentralizado(String.format("Resgates: %d de %d vitimas", metricas.getResgates(), apareceram), y, 1.4f);
        game.escreverCentralizado(String.format("Velocidade: %.0f/100   (%.1f s por resgate)",
                metricas.getNotaDeVelocidade(), metricas.getTempoMedioPorResgate()), y, 1.4f);
        game.escreverCentralizado(String.format("Precisao: %.0f/100", metricas.getPrecisao()), y, 1.4f);
        game.escreverCentralizado(String.format("Pico de forca: %.0f%%", metricas.getPicoDeForca()), y, 1.4f);
        game.escreverCentralizado(String.format("Pontuacao: %.0f/100", metricas.getPontuacao()), 160f, 2.2f, Color.YELLOW);

        Fase proxima = fase.getProxima();
        String proximoPasso = (proxima != null) ? "ENTER = " + proxima.nome : "ENTER = ver o resultado final";
        game.escreverCentralizado(proximoPasso, 90f, 1.4f);

        game.batch.end();
    }
}