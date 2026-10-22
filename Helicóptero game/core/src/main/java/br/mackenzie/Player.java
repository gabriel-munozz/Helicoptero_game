package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Player {

    private static final float LARGURA = 120f;

    private static final float PRESSAO_BAIXA = 0.35f;
    private static final float PRESSAO_ALTA = 0.65f;

    private static final float VELOCIDADE_VERTICAL = 200f; // pixels por segundo (sempre a mesma)

    private final Texture textura;
    private final float altura;
    private float x;
    private float y;

    public Player() {
        textura = new Texture("player.png");

        altura = LARGURA * textura.getHeight() / textura.getWidth();

        x = 200f;
        y = (Main.ALTURA - altura) / 2f;
    }

    public void atualizar(float pressao, float delta) {
        float velocidade = 0f;

        if (pressao < PRESSAO_BAIXA) {
            velocidade = -VELOCIDADE_VERTICAL; // desce
        } else if (pressao > PRESSAO_ALTA) {
            velocidade = VELOCIDADE_VERTICAL;  // sobe
        }

        y += velocidade * delta;

        y = MathUtils.clamp(y, 0f, Main.ALTURA - altura);
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, x, y, LARGURA, altura);
    }

    public void dispose() {
        textura.dispose();
    }
}