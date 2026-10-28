package br.mackenzie;

public class SensorArduino implements SensorPressao {

    private ArduinoReaderThread arduinoReader;

    public SensorArduino(String porta) {

        arduinoReader = new ArduinoReaderThread(porta);
        arduinoReader.start();
    }

    @Override
    public float ler(float delta) {

        String dado = arduinoReader.getLastData();

        try {

            int valor = Integer.parseInt(dado);

            float pressao = valor / 1023f;

            System.out.println("Arduino: " + valor + " | Pressao: " + pressao);

            return pressao;

        } catch (NumberFormatException e) {

            return 0f;
        }
    }

    public void fechar() {

        arduinoReader.stopReading();
    }
}