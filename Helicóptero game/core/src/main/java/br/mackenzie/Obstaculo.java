package br.mackenzie;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Obstaculo {

    private static final float ALTURA = 230f;
    private static final float DISTANCIA_MINIMA_DA_VITIMA = 250f;

    private final Texture textura;
    private final Rectangle area = new Rectangle();
    private final Rectangle colisao = new Rectangle();

    public Obstaculo(float xParaEvitar) {
        textura = new Texture("predio.png");
        float largura = ALTURA * textura.getWidth() / textura.getHeight();
        area.set(0f, Main.ALTURA_DO_CHAO, largura, ALTURA);
        reaparecer(xParaEvitar);
    }

    public void atualizar(float deslocamento) {
        area.x -= deslocamento;
    }

    public boolean saiuDaTela() {
        return area.x + area.width < 0f;
    }

    public void reaparecer(float xParaEvitar) {
        for (int i = 0; i < 20; i++) {
            area.x = Main.LARGURA + MathUtils.random(150f, 900f);
            if (Math.abs(area.x - xParaEvitar) >= DISTANCIA_MINIMA_DA_VITIMA) {
                return;
            }
        }
    }

    public Rectangle getArea() {
        return area;
    }

    public Rectangle getAreaDeColisao() {
        float margemLado = area.width * 0.15f;
        float margemTopo = area.height * 0.1f;
        colisao.set(area.x + margemLado, area.y, area.width - 2f * margemLado, area.height - margemTopo);
        return colisao;
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, area.x, area.y, area.width, area.height);
    }

    public void dispose() {
        textura.dispose();
    }
}