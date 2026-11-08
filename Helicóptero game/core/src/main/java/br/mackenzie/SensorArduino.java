package br.mackenzie;

import com.fazecast.jSerialComm.SerialPort;

public class SensorArduino implements SensorPressao {

    private final ArduinoReaderThread arduinoReader;
    private final String nomeDaPorta;

    public SensorArduino(SerialPort porta) {
        this.nomeDaPorta = porta.getSystemPortName();
        this.arduinoReader = new ArduinoReaderThread(porta);
        this.arduinoReader.start();
    }

    private static void listarPortasNoConsole() {
        System.out.println("Nenhum Arduino reconhecido. Portas encontradas:");
        for (SerialPort porta : SerialPort.getCommPorts()) {
            System.out.println(" - " + porta.getSystemPortName() + " : " + porta.getDescriptivePortName());
        }
    }

    private boolean aguardarDados(long limiteMs) {
        long fim = System.currentTimeMillis() + limiteMs;

        while (System.currentTimeMillis() < fim) {
            if (arduinoReader.recebeuDados()) {
                return true;
            }
            if (!arduinoReader.isAlive()) {
                return false;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    public static SensorArduino conectarAutomaticamente() {
        for (SerialPort porta : SerialPort.getCommPorts()) {
            String nome = porta.getDescriptivePortName().toLowerCase();
            if (nome.contains("bluetooth")) {
                continue;
            }

            SensorArduino sensor = new SensorArduino(porta);
            if (sensor.aguardarDados(3000)) {
                System.out.println("Arduino encontrado na porta " + sensor.getNomeDaPorta());
                return sensor;
            }

            System.out.println("A porta " + sensor.getNomeDaPorta() + " nao mandou dados.");
            sensor.fechar();
        }

        listarPortasNoConsole();
        return null;
    }

    @Override
    public float ler(float delta) {

        String dado = arduinoReader.getLastData();

        try {

            int valor = Integer.parseInt(dado);

            return Math.min(1f, Math.max(0f, valor / 1023f));

        } catch (NumberFormatException e) {

            return 0f;
        }
    }

    public String getNomeDaPorta() {
        return nomeDaPorta;
    }

    public void fechar() {

        arduinoReader.stopReading();
    }
}