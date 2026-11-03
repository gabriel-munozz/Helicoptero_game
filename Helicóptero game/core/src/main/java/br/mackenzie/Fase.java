package br.mackenzie;
public class Fase {

    public static final Fase[] TODAS = {
            new Fase("Fase 1", 150f, 5, 1.0f,
                    TipoDeObstaculo.PREDIO),
            new Fase("Fase 2", 200f, 8, 0.75f,
                    TipoDeObstaculo.PREDIO, TipoDeObstaculo.PASSARO),
            new Fase("Fase 3", 250f, 10, 0.6f,
                    TipoDeObstaculo.PREDIO, TipoDeObstaculo.PASSARO, TipoDeObstaculo.GUINDASTE)
    };

    public final String nome;
    public final float velocidadeDoCenario;
    public final int vitimasParaPassar;
    public final float espacoEntreObstaculos;
    public final TipoDeObstaculo[] obstaculos;

    public Fase(String nome, float velocidadeDoCenario, int vitimasParaPassar,
                float espacoEntreObstaculos, TipoDeObstaculo... obstaculos) {
        this.nome = nome;
        this.velocidadeDoCenario = velocidadeDoCenario;
        this.vitimasParaPassar = vitimasParaPassar;
        this.espacoEntreObstaculos = espacoEntreObstaculos;
        this.obstaculos = obstaculos;
    }

    public Fase getProxima() {
        for (int i = 0; i < TODAS.length - 1; i++) {
            if (TODAS[i] == this) {
                return TODAS[i + 1];
            }
        }
        return null;
    }
}