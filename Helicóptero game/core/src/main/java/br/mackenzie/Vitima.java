package br.mackenzie;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Vitima {

    private static final float ALTURA = 55f;
    private static final float AJUSTE_Y = 5f;

    private final Texture textura;
    private final Rectangle area = new Rectangle();

    public Vitima(float xMinimo) {
        textura = new Texture("vitima.png");
        float largura = ALTURA * textura.getWidth() / textura.getHeight();
        area.set(0f, Main.ALTURA_DO_CHAO + AJUSTE_Y, largura, ALTURA);
        reaparecer(xMinimo);
    }

    public void atualizar(float deslocamento) {
        area.x -= deslocamento;
    }

    public boolean saiuDaTela() {
        return area.x + area.width < 0f;
    }

    public void reaparecer(float xMinimo) {
        float x = Main.LARGURA + MathUtils.random(100f, 600f);
        area.x = Math.max(x, xMinimo);
    }

    public Rectangle getArea() {
        return area;
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, area.x, area.y, area.width, area.height);
    }

    public void dispose() {
        textura.dispose();
    }
}