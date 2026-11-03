package br.mackenzie;
import java.util.PriorityQueue;

public class Metricas {

    private static final float DISTANCIA_TIPICA_ENTRE_RESGATES = 1100f;

    private static final int LEITURAS_DO_PICO = 30;

    private final String nomeDaFase;
    private final float velocidadeDoCenario;

    private float tempoDaFase;
    private int resgates;
    private int vitimasPerdidas;

    private final PriorityQueue<Float> maioresLeituras = new PriorityQueue<>();

    public Metricas(String nomeDaFase, float velocidadeDoCenario) {
        this.nomeDaFase = nomeDaFase;
        this.velocidadeDoCenario = velocidadeDoCenario;
    }

    public void registrarLeitura(float pressao) {
        if (maioresLeituras.size() < LEITURAS_DO_PICO) {
            maioresLeituras.add(pressao);
        } else if (pressao > maioresLeituras.peek()) {
            maioresLeituras.poll();
            maioresLeituras.add(pressao);
        }
    }

    public void registrarResgate() {
        resgates++;
    }

    public void registrarVitimaPerdida() {
        vitimasPerdidas++;
    }

    public void finalizar(float tempoDaFase) {
        this.tempoDaFase = tempoDaFase;
    }

    public String getNomeDaFase() {
        return nomeDaFase;
    }

    public float getTempoDaFase() {
        return tempoDaFase;
    }

    public int getResgates() {
        return resgates;
    }

    public int getVitimasPerdidas() {
        return vitimasPerdidas;
    }

    public float getTempoMedioPorResgate() {
        return resgates == 0 ? 0f : tempoDaFase / resgates;
    }

    public float getNotaDeVelocidade() {
        float tempoMedio = getTempoMedioPorResgate();
        if (tempoMedio <= 0f) {
            return 0f;
        }
        float tempoDeReferencia = DISTANCIA_TIPICA_ENTRE_RESGATES / velocidadeDoCenario;
        return 100f * Math.max(0f, Math.min(1f, tempoDeReferencia / tempoMedio));
    }

    public float getPrecisao() {
        int apareceram = resgates + vitimasPerdidas;
        return apareceram == 0 ? 0f : 100f * resgates / apareceram;
    }

    public float getPicoDeForca() {
        if (maioresLeituras.isEmpty()) {
            return 0f;
        }
        float soma = 0f;
        for (float leitura : maioresLeituras) {
            soma += leitura;
        }
        return 100f * soma / maioresLeituras.size();
    }

    public float getPontuacao() {
        return (getNotaDeVelocidade() + getPrecisao()) / 2f;
    }
}