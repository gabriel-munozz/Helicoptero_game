package br.mackenzie;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstaculo {

    private final TipoDeObstaculo tipo;
    private final Texture textura;
    private final Rectangle area = new Rectangle();
    private final Rectangle colisao = new Rectangle();

    public Obstaculo(TipoDeObstaculo tipo, Texture textura, float x, float y) {
        this.tipo = tipo;
        this.textura = textura;
        float largura = tipo.altura * textura.getWidth() / textura.getHeight();
        area.set(x, y, largura, tipo.altura);
    }

    public static float velocidadeTotal(TipoDeObstaculo tipo, float velocidadeDoCenario) {
        return velocidadeDoCenario + tipo.velocidadeExtra;
    }

    public void atualizar(float delta, float velocidadeDoCenario) {
        area.x -= velocidadeTotal(tipo, velocidadeDoCenario) * delta;
    }

    public boolean saiuDaTela() {
        return area.x + area.width < 0f;
    }

    public TipoDeObstaculo getTipo() {
        return tipo;
    }

    public Rectangle getArea() {
        return area;
    }

    public Rectangle getAreaDeColisao() {
        float margemLado = area.width * tipo.margemLateral;
        float margemVertical = area.height * 0.1f;

        if (tipo.noChao) {
            colisao.set(area.x + margemLado, area.y,
                    area.width - 2f * margemLado, area.height - margemVertical);
        } else {
            colisao.set(area.x + margemLado, area.y + margemVertical,
                    area.width - 2f * margemLado, area.height - 2f * margemVertical);
        }
        return colisao;
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, area.x, area.y, area.width, area.height);
    }
}