package br.mackenzie;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;

public class GameScreen extends ScreenAdapter {

    private static final float TEMPO_FASE_CONCLUIDA = 1.2f;

    private final Main game;
    private final Fase fase;
    private final Partida partida;
    private final Metricas metricas;
    private final Player player;
    private final Vitima vitima;
    private final GeradorDeObstaculos obstaculos;

    private final float velocidadeDoCenario;
    private float deslocamentoDoFundo = 0f;

    private float tempoDaFase = 0f;
    private boolean pausado = false;

    private boolean faseConcluida = false;
    private float tempoFaseConcluida = 0f;

    public GameScreen(Main game, Fase fase) {
        this(game, fase, new Partida());
    }

    public GameScreen(Main game, Fase fase, Partida partida) {
        this.game = game;
        this.fase = fase;
        this.partida = partida;
        this.metricas = new Metricas(fase.nome, fase.velocidadeDoCenario);
        this.velocidadeDoCenario = fase.velocidadeDoCenario;
        this.player = new Player();
        this.obstaculos = new GeradorDeObstaculos(fase);
        this.vitima = new Vitima(obstaculos.getXMinimoParaVitima());
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
            game.setScreen(new GameScreen(game, fase, partida));
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
                game.setScreen(new ResumoFaseScreen(game, fase, partida, metricas));
                return true;
            }
            return false;
        }

        tempoDaFase += delta;
        metricas.registrarLeitura(pressao);

        vitima.atualizar(velocidadeDoCenario * delta);
        obstaculos.atualizar(delta, vitima.getArea().x, player.getArea());

        if (vitima.saiuDaTela()) {
            metricas.registrarVitimaPerdida();
            vitima.reaparecer(obstaculos.getXMinimoParaVitima());
        }

        if (obstaculos.bateu(player.getAreaDeColisao())) {
            game.somColisao.play(0.7f);
            game.setScreen(new GameOverScreen(game, fase, partida));
            return true;
        }

        if (player.getArea().overlaps(vitima.getArea())) {
            metricas.registrarResgate();
            vitima.reaparecer(obstaculos.getXMinimoParaVitima());

            if (metricas.getResgates() >= fase.vitimasParaPassar) {
                faseConcluida = true;
                tempoFaseConcluida = TEMPO_FASE_CONCLUIDA;

                metricas.finalizar(tempoDaFase);
                partida.adicionar(metricas);
                HistoricoDePartidas.salvar(partida, metricas);
            }
        }
        return false;
    }

    private void desenhar() {
        game.prepararCamera();
        game.batch.begin();

        game.batch.draw(game.background, -deslocamentoDoFundo, 0, Main.LARGURA, Main.ALTURA);
        game.batch.draw(game.background, Main.LARGURA - deslocamentoDoFundo, 0, Main.LARGURA, Main.ALTURA);
        if (!faseConcluida) {
            obstaculos.desenhar(game.batch);
            vitima.desenhar(game.batch);
        }
        player.desenhar(game.batch);

        game.escrever(fase.nome + "     Resgates: " + metricas.getResgates() + "/" + fase.vitimasParaPassar,
                15f, Main.ALTURA - 15f, 1.3f, Color.BLACK);

        if (faseConcluida) {
            game.escurecerTela(0.45f);
            game.escreverCentralizado("FASE CONCLUIDA!", 300f, 2.6f, Color.YELLOW);
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
        obstaculos.dispose();
    }
}