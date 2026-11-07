package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GeradorDeObstaculos {

    private static final float DISTANCIA_MINIMA = 330f;
    private static final float FOLGA_MAXIMA = 400f;
    private static final float TEMPO_MINIMO_PASSARO_E_CHAO = 0.4f;

    private static final float PASSARO_Y_MIN = 270f;
    private static final float PASSARO_Y_MAX = 330f;

    private final TipoDeObstaculo[] tipos;
    private final float velocidadeDoCenario;
    private final float espacoEntreObstaculos;
    private final Map<TipoDeObstaculo, Texture> texturas = new EnumMap<>(TipoDeObstaculo.class);
    private final List<Obstaculo> obstaculos = new ArrayList<>();

    private float distanciaDesdeUltimo = -160f;
    private float distanciaAteProximo;
    private TipoDeObstaculo proximoTipo;

    public GeradorDeObstaculos(Fase fase) {
        this.tipos = fase.obstaculos;
        this.velocidadeDoCenario = fase.velocidadeDoCenario;
        this.espacoEntreObstaculos = fase.espacoEntreObstaculos;

        for (TipoDeObstaculo tipo : tipos) {
            texturas.put(tipo, new Texture(tipo.arquivo));
        }
        sortearProximo();
    }

    public void atualizar(float delta, float xDaVitima, Rectangle areaDoPlayer) {
        for (Obstaculo o : obstaculos) {
            o.atualizar(delta, velocidadeDoCenario);
        }
        distanciaDesdeUltimo += velocidadeDoCenario * delta;

        Iterator<Obstaculo> it = obstaculos.iterator();
        while (it.hasNext()) {
            if (it.next().saiuDaTela()) {
                it.remove();
            }
        }

        float xNovo = Main.LARGURA + 20f;
        if (distanciaDesdeUltimo >= distanciaAteProximo
                && podeNascer(proximoTipo, xNovo, xDaVitima, areaDoPlayer)) {
            criar(proximoTipo, xNovo);
        }
    }

    public boolean bateu(Rectangle areaDoPlayer) {
        for (Obstaculo o : obstaculos) {
            if (o.getAreaDeColisao().overlaps(areaDoPlayer)) {
                return true;
            }
        }
        return false;
    }

    public float getXMinimoParaVitima() {
        float minimo = 0f;
        for (Obstaculo o : obstaculos) {
            if (o.getTipo().noChao) {
                float borda = o.getArea().x + o.getArea().width + o.getTipo().distanciaDaVitima;
                minimo = Math.max(minimo, borda);
            }
        }
        return minimo;
    }

    public void desenhar(SpriteBatch batch) {
        for (Obstaculo o : obstaculos) {
            o.desenhar(batch);
        }
    }

    public void dispose() {
        for (Texture t : texturas.values()) {
            t.dispose();
        }
    }

    private boolean podeNascer(TipoDeObstaculo tipo, float xNovo, float xDaVitima, Rectangle player) {

        if (tipo.distanciaDaVitima > 0f && Math.abs(xNovo - xDaVitima) < tipo.distanciaDaVitima) {
            return false;
        }

        float velocidadeNovo = Obstaculo.velocidadeTotal(tipo, velocidadeDoCenario);
        float larguraNovo = larguraDe(tipo);
        float entradaNovo = (xNovo - (player.x + player.width)) / velocidadeNovo;
        float saidaNovo = (xNovo + larguraNovo - player.x) / velocidadeNovo;

        for (Obstaculo o : obstaculos) {
            boolean umEPassaroOOutroNao =
                    (o.getTipo() == TipoDeObstaculo.PASSARO) != (tipo == TipoDeObstaculo.PASSARO);
            if (!umEPassaroOOutroNao) {
                continue;
            }

            float velocidade = Obstaculo.velocidadeTotal(o.getTipo(), velocidadeDoCenario);
            float entrada = (o.getArea().x - (player.x + player.width)) / velocidade;
            float saida = (o.getArea().x + o.getArea().width - player.x) / velocidade;

            float tempoLivre = Math.max(entradaNovo - saida, entrada - saidaNovo);
            if (tempoLivre < TEMPO_MINIMO_PASSARO_E_CHAO) {
                return false;
            }
        }
        return true;
    }

    private void criar(TipoDeObstaculo tipo, float x) {
        float y = tipo.noChao ? Main.ALTURA_DO_CHAO : MathUtils.random(PASSARO_Y_MIN, PASSARO_Y_MAX);

        Obstaculo novo = new Obstaculo(tipo, texturas.get(tipo), x, y);
        obstaculos.add(novo);

        distanciaDesdeUltimo = -novo.getArea().width;
        sortearProximo();
    }

    private void sortearProximo() {
        proximoTipo = tipos[MathUtils.random(tipos.length - 1)];
        distanciaAteProximo = (DISTANCIA_MINIMA + MathUtils.random(0f, FOLGA_MAXIMA)) * espacoEntreObstaculos;
    }

    private float larguraDe(TipoDeObstaculo tipo) {
        Texture t = texturas.get(tipo);
        return tipo.altura * t.getWidth() / t.getHeight();
    }
}