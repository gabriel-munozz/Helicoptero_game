package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private static final float LARGURA = 120f;

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

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, x, y, LARGURA, altura);
    }

    public void dispose() {
        textura.dispose();
    }
}
