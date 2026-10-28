package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;

public class GameScreen extends ScreenAdapter {

    private static final float TEMPO_FASE_CONCLUIDA = 2f;

    private final Main game;
    private final Fase fase;
    private final Player player;
    private final Vitima vitima;
    private final Obstaculo obstaculo;

    private final float velocidadeDoCenario;
    private float deslocamentoDoFundo = 0f;

    private int resgates = 0;
    private boolean pausado = false;

    private boolean faseConcluida = false;
    private float tempoFaseConcluida = 0f;

    public GameScreen(Main game, Fase fase) {
        this.game = game;
        this.fase = fase;
        this.velocidadeDoCenario = fase.velocidadeDoCenario;
        this.player = new Player();
        this.vitima = new Vitima(-10000f);
        this.obstaculo = new Obstaculo(vitima.getArea().x);
    }

    @Override
    public void show() {
        game.somHelicoptero.play();
    }

    @Override
    public void render(float delta) {
        if (tratarTeclas()) return;

        if (!pausado) {
            if (atualizar(delta)) return;
        }
        desenhar();
    }

    private boolean tratarTeclas() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game, fase));
            return true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pausado = !pausado;

            if (pausado){
                game.somHelicoptero.pause();
            } else {
                game.somHelicoptero.play();
            }
        }
        return false;
    }

    private boolean atualizar(float delta) {
        float pressao = game.sensor.ler(delta);
        player.atualizar(pressao, delta);

        deslocamentoDoFundo = (deslocamentoDoFundo + velocidadeDoCenario * delta) % Main.LARGURA;

        if (faseConcluida) {
            tempoFaseConcluida -= delta;
            if (tempoFaseConcluida <= 0f) {
                irParaProximaFase();
                return true;
            }
            return false;
        }

        vitima.atualizar(velocidadeDoCenario * delta);
        obstaculo.atualizar(velocidadeDoCenario * delta);

        if (vitima.saiuDaTela()) {
            vitima.reaparecer(obstaculo.getArea().x);
        }
        if (obstaculo.saiuDaTela()) {
            obstaculo.reaparecer(vitima.getArea().x);
        }

        if (player.getAreaDeColisao().overlaps(obstaculo.getAreaDeColisao())) {
            game.somColisao.play(0.7f);
            game.setScreen(new GameOverScreen(game, fase));
            return true;
        }

        if (player.getArea().overlaps(vitima.getArea())) {
            resgates++;
            vitima.reaparecer(obstaculo.getArea().x);

            if (resgates >= fase.vitimasParaPassar) {
                faseConcluida = true;
                tempoFaseConcluida = TEMPO_FASE_CONCLUIDA;
            }
        }
        return false;
    }

    private void irParaProximaFase() {
        Fase proxima = fase.getProxima();
        if (proxima != null) {
            game.setScreen(new GameScreen(game, proxima));
        } else {
            game.setScreen(new FimScreen(game));
        }
    }

    private void desenhar() {
        game.prepararCamera();
        game.batch.begin();

        game.batch.draw(game.background, -deslocamentoDoFundo, 0, Main.LARGURA, Main.ALTURA);
        game.batch.draw(game.background, Main.LARGURA - deslocamentoDoFundo, 0, Main.LARGURA, Main.ALTURA);
        if (!faseConcluida) {
            obstaculo.desenhar(game.batch);
            vitima.desenhar(game.batch);
        }
        player.desenhar(game.batch);

        game.escrever(fase.nome + "     Resgates: " + resgates + "/" + fase.vitimasParaPassar,
                15f, Main.ALTURA - 15f, 1.3f, Color.BLACK);

        if (faseConcluida) {
            game.escurecerTela(0.45f);
            game.escreverCentralizado("FASE CONCLUIDA!", 320f, 2.6f, Color.YELLOW);
            Fase proxima = fase.getProxima();
            if (proxima != null) {
                game.escreverCentralizado("Proxima: " + proxima.nome, 250f, 1.5f);
            }
        }

        if (pausado) {
            game.escurecerTela(0.6f);
            game.escreverCentralizado("PAUSADO", 350f, 2.6f);
            game.escreverCentralizado(fase.nome, 290f, 1.6f);
            game.escreverCentralizado("P = continuar", 230f, 1.5f);
            game.escreverCentralizado("R = reiniciar", 195f, 1.5f);
            game.escreverCentralizado("ESC = menu inicial", 160f, 1.5f);
        }

        game.batch.end();
    }

    @Override
    public void hide() {
        game.somHelicoptero.stop();
        player.dispose();
        vitima.dispose();
        obstaculo.dispose();
    }
}