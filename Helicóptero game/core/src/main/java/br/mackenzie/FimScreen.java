package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import java.util.List;

public class FimScreen extends ScreenAdapter {

    private static final float COL_FASE = 40f;
    private static final float COL_TEMPO = 190f;
    private static final float COL_VELOCIDADE = 310f;
    private static final float COL_PRECISAO = 440f;
    private static final float COL_PICO = 570f;
    private static final float COL_PONTUACAO = 660f;

    private final Main game;
    private final Partida partida;

    public FimScreen(Main game, Partida partida) {
        this.game = game;
        this.partida = partida;
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
        game.escurecerTela(0.7f);

        game.escreverCentralizado("PARABENS! VOCE COMPLETOU AS FASES", 445f, 2.0f, Color.YELLOW);

        float y = 375f;
        game.escrever("Fase", COL_FASE, y, 1.2f, Color.YELLOW);
        game.escrever("Tempo", COL_TEMPO, y, 1.2f, Color.YELLOW);
        game.escrever("Velocidade", COL_VELOCIDADE, y, 1.2f, Color.YELLOW);
        game.escrever("Precisao", COL_PRECISAO, y, 1.2f, Color.YELLOW);
        game.escrever("Pico", COL_PICO, y, 1.2f, Color.YELLOW);
        game.escrever("Pontuacao", COL_PONTUACAO, y, 1.2f, Color.YELLOW);

        List<Metricas> resultados = partida.getResultados();
        for (int i = 0; i < resultados.size(); i++) {
            Metricas m = resultados.get(i);
            y = 340f - i * 34f;
            game.escrever(m.getNomeDaFase(), COL_FASE, y, 1.2f, Color.WHITE);
            game.escrever(String.format("%.1f s", m.getTempoDaFase()), COL_TEMPO, y, 1.2f, Color.WHITE);
            game.escrever(String.format("%.0f", m.getNotaDeVelocidade()), COL_VELOCIDADE, y, 1.2f, Color.WHITE);
            game.escrever(String.format("%.0f", m.getPrecisao()), COL_PRECISAO, y, 1.2f, Color.WHITE);
            game.escrever(String.format("%.0f%%", m.getPicoDeForca()), COL_PICO, y, 1.2f, Color.WHITE);
            game.escrever(String.format("%.0f", m.getPontuacao()), COL_PONTUACAO, y, 1.2f, Color.WHITE);
        }

        game.escreverCentralizado(String.format("Pontuacao media: %.0f/100     Tempo total: %.1f s",
                partida.getPontuacaoMedia(), partida.getTempoTotal()), 215f, 1.5f, Color.YELLOW);

        if (resultados.size() >= 2) {
            Metricas primeira = resultados.get(0);
            Metricas ultima = resultados.get(resultados.size() - 1);
            game.escreverCentralizado(String.format("Evolucao da precisao: %.0f -> %.0f  (%+.0f)",
                    primeira.getPrecisao(), ultima.getPrecisao(),
                    ultima.getPrecisao() - primeira.getPrecisao()), 175f, 1.3f, Color.WHITE);
        }

        game.escreverCentralizado("Resultados salvos em: " + HistoricoDePartidas.getCaminho(), 120f, 0.95f);
        game.escreverCentralizado("ENTER = voltar ao menu", 75f, 1.4f);

        game.batch.end();
    }
}