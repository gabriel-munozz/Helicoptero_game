package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Vitima {

    private static final float ALTURA = 50f;

    private final Texture textura;
    private final Rectangle area = new Rectangle();

    public Vitima() {
        textura = criarTextura();
        float largura = ALTURA * textura.getWidth() / textura.getHeight();
        area.set(0f, Main.ALTURA_DO_CHAO, largura, ALTURA);
        reaparecer();
    }

    public void atualizar(float deslocamento) {
        area.x -= deslocamento;

        if (area.x + area.width < 0f) {
            reaparecer();
        }
    }

    public void reaparecer() {
        area.x = Main.LARGURA + MathUtils.random(100f, 400f);
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

    private Texture criarTextura() {
        if (Gdx.files.internal("vitima.png").exists()) {
            return new Texture("vitima.png");
        }
        // imagem provisória: corpo laranja e cabeça
        Pixmap pm = new Pixmap(20, 30, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 0.55f, 0.1f, 1f);
        pm.fillRectangle(5, 12, 10, 18);
        pm.setColor(1f, 0.8f, 0.6f, 1f);
        pm.fillCircle(10, 6, 6);
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }
}