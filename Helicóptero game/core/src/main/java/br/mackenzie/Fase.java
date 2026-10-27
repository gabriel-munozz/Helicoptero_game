package br.mackenzie;
public class Fase {

    public static final Fase[] TODAS = {
            new Fase("Fase 1", 150f, 4),
            new Fase("Fase 2", 200f, 6),
            new Fase("Fase 3", 250f, 8)
    };

    public final String nome;
    public final float velocidadeDoCenario;
    public final int vitimasParaPassar;

    public Fase(String nome, float velocidadeDoCenario, int vitimasParaPassar) {
        this.nome = nome;
        this.velocidadeDoCenario = velocidadeDoCenario;
        this.vitimasParaPassar = vitimasParaPassar;
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