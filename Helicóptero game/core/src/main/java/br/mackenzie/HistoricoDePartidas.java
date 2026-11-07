package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.time.LocalDateTime;
import java.util.Locale;

public final class HistoricoDePartidas {

    private static final String ARQUIVO = "HelicopteroGame/historico_partidas.csv";
    private static final String CABECALHO =
            "data_hora;partida;fase;tempo_s;resgates;vitimas_perdidas;tempo_medio_por_resgate_s;"
                    + "precisao_pct;pico_forca_pct;nota_velocidade;pontuacao";
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private HistoricoDePartidas() {
    }

    public static void salvar(Partida partida, Metricas m) {
        try {
            FileHandle arquivo = Gdx.files.external(ARQUIVO);

            if (!arquivo.exists()) {
                arquivo.writeString(CABECALHO + "\n", false, "UTF-8");
            }

            String agora = LocalDateTime.now().format(Partida.FORMATO_DE_DATA);
            String linha = String.join(";",
                    agora,
                    partida.getId(),
                    m.getNomeDaFase(),
                    numero(m.getTempoDaFase()),
                    String.valueOf(m.getResgates()),
                    String.valueOf(m.getVitimasPerdidas()),
                    numero(m.getTempoMedioPorResgate()),
                    numero(m.getPrecisao()),
                    numero(m.getPicoDeForca()),
                    numero(m.getNotaDeVelocidade()),
                    numero(m.getPontuacao()));

            arquivo.writeString(linha + "\n", true, "UTF-8");

        } catch (Exception e) {
            Gdx.app.error("Historico", "Nao foi possivel salvar o historico: " + e.getMessage());
        }
    }

    public static String getCaminho() {
        return Gdx.files.external(ARQUIVO).file().getAbsolutePath();
    }

    private static String numero(float valor) {
        return String.format(PT_BR, "%.1f", valor);
    }
}