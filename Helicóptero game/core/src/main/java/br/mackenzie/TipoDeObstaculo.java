package br.mackenzie;
import com.badlogic.gdx.graphics.Color;

public enum TipoDeObstaculo {

    PREDIO("predio.png", 230f, true, 0.15f, 0.60f, new Color(0.60f, 0.25f, 0.20f, 1f), 0f, 250f),
    GUINDASTE("guindaste.png", 250f, true, 0.20f, 0.50f, new Color(0.95f, 0.75f, 0.10f, 1f), 0f, 330f),
    PASSARO("passaro.png", 75f, false, 0.15f, 1.40f, new Color(0.30f, 0.30f, 0.35f, 1f), 110f, 0f);

    public final String arquivo;
    public final float altura;
    public final boolean noChao;
    public final float margemLateral;
    public final float proporcaoReserva;
    public final Color corReserva;
    public final float velocidadeExtra;
    public final float distanciaDaVitima;
    TipoDeObstaculo(String arquivo, float altura, boolean noChao, float margemLateral,
                    float proporcaoReserva, Color corReserva,
                    float velocidadeExtra, float distanciaDaVitima) {
        this.arquivo = arquivo;
        this.altura = altura;
        this.noChao = noChao;
        this.margemLateral = margemLateral;
        this.proporcaoReserva = proporcaoReserva;
        this.corReserva = corReserva;
        this.velocidadeExtra = velocidadeExtra;
        this.distanciaDaVitima = distanciaDaVitima;
    }
}