package br.mackenzie;

public enum TipoDeObstaculo {

    PREDIO("predio.png", 230f, true, 0.15f, 0f, 250f),
    GUINDASTE("guindaste.png", 250f, true, 0.20f, 0f, 330f),
    PASSARO("passaro.png", 75f, false, 0.15f, 110f, 0f);

    public final String arquivo;
    public final float altura;
    public final boolean noChao;
    public final float margemLateral;
    public final float velocidadeExtra;
    public final float distanciaDaVitima;

    TipoDeObstaculo(String arquivo, float altura, boolean noChao, float margemLateral,
                    float velocidadeExtra, float distanciaDaVitima) {
        this.arquivo = arquivo;
        this.altura = altura;
        this.noChao = noChao;
        this.margemLateral = margemLateral;
        this.velocidadeExtra = velocidadeExtra;
        this.distanciaDaVitima = distanciaDaVitima;
    }
}