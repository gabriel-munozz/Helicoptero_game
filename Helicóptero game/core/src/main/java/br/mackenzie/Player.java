package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    private static final float LARGURA = 120f;

    private static final String FRAME_1 = "player1.png";
    private static final String FRAME_2 = "player2.png";

    private static final float DURACAO_DO_FRAME = 0.06f;
    private static final float INCLINACAO_MAXIMA = 8f;
    private static final float SUAVIDADE_DA_INCLINACAO = 8f;

    private static final float PRESSAO_BAIXA = 0.35f;
    private static final float PRESSAO_ALTA = 0.65f;

    private static final float VELOCIDADE_VERTICAL = 200f;

    private final Texture textura1;
    private final Texture textura2;
    private final Animation<TextureRegion> animacao;
    private final float altura;
    private float x;
    private float y;
    private float tempoDaAnimacao = 0f;
    private float inclinacao = 0f;
    private final Rectangle area = new Rectangle();
    private final Rectangle colisao = new Rectangle();

    public Player() {
        textura1 = new Texture(FRAME_1);
        textura2 = new Texture(FRAME_2);

        animacao = new Animation<TextureRegion>(DURACAO_DO_FRAME,
                new TextureRegion(textura1), new TextureRegion(textura2));
        animacao.setPlayMode(Animation.PlayMode.LOOP);

        altura = LARGURA * textura1.getHeight() / textura1.getWidth();

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

        float yAntes = y;
        y += velocidade * delta;

        y = MathUtils.clamp(y, Main.ALTURA_DO_CHAO + 11f, Main.ALTURA - altura);

        tempoDaAnimacao += delta;

        float movimento = y - yAntes;
        float inclinacaoAlvo = 0f;
        if (movimento > 0f) {
            inclinacaoAlvo = INCLINACAO_MAXIMA;
        } else if (movimento < 0f) {
            inclinacaoAlvo = -INCLINACAO_MAXIMA;
        }
        inclinacao += (inclinacaoAlvo - inclinacao) * Math.min(1f, SUAVIDADE_DA_INCLINACAO * delta);
    }

    public Rectangle getArea() {
        area.set(x, y, LARGURA, altura);
        return area;
    }

    public Rectangle getAreaDeColisao() {
        float margemLado = LARGURA * 0.15f;
        float margemAltura = altura * 0.2f;
        colisao.set(x + margemLado, y + margemAltura, LARGURA - 2f * margemLado, altura - 2f * margemAltura);
        return colisao;
    }

    public void desenhar(SpriteBatch batch) {
        TextureRegion frame = animacao.getKeyFrame(tempoDaAnimacao, true);

        batch.draw(frame, x, y, LARGURA / 2f, altura / 2f, LARGURA, altura, 1f, 1f, inclinacao);
    }

    public void dispose() {
        textura1.dispose();
        textura2.dispose();
    }
}