package br.mackenzie;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Partida {

    private final String id;
    private final List<Metricas> resultados = new ArrayList<>();

    public Partida() {
        this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void adicionar(Metricas metricas) {
        resultados.add(metricas);
    }

    public List<Metricas> getResultados() {
        return resultados;
    }

    public Metricas getAnterior(Metricas atual) {
        int i = resultados.indexOf(atual);
        return i > 0 ? resultados.get(i - 1) : null;
    }

    public String getId() {
        return id;
    }

    public float getPontuacaoMedia() {
        if (resultados.isEmpty()) {
            return 0f;
        }
        float soma = 0f;
        for (Metricas m : resultados) {
            soma += m.getPontuacao();
        }
        return soma / resultados.size();
    }

    public float getTempoTotal() {
        float total = 0f;
        for (Metricas m : resultados) {
            total += m.getTempoDaFase();
        }
        return total;
    }
}