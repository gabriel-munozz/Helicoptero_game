package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class SensorSimulado implements SensorPressao {

    @Override
    public float ler(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            return 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            return 0.5f;
        }
        return 0.0f;
    }
}