package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    private static final float LARGURA = 120f;

    private static final float PRESSAO_BAIXA = 0.35f;
    private static final float PRESSAO_ALTA = 0.65f;

    private static final float VELOCIDADE_VERTICAL = 200f;

    private final Texture textura;
    private final float altura;
    private float x;
    private float y;
    private final Rectangle area = new Rectangle();

    public Player() {
        textura = new Texture("player.png");

        altura = LARGURA * textura.getHeight() / textura.getWidth();

        x = 200f;
        y = (Main.ALTURA - altura) / 2f;
    }

    public void atualizar(float pressao, float delta) {
        float velocidade = 0f;

        if (pressao < PRESSAO_BAIXA) {
            velocidade = -VELOCIDADE_VERTICAL;
        } else if (pressao > PRESSAO_ALTA) {
            velocidade = VELOCIDADE_VERTICAL;
        }

        y += velocidade * delta;

        y = MathUtils.clamp(y, Main.ALTURA_DO_CHAO, Main.ALTURA - altura);
    }

    public Rectangle getArea() {
        area.set(x, y, LARGURA, altura);
        return area;
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, x, y, LARGURA, altura);
    }

    public void dispose() {
        textura.dispose();
    }
}