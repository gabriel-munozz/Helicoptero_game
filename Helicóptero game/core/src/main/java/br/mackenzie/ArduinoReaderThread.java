package br.mackenzie;
import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;

public class ArduinoReaderThread extends Thread {

    private volatile String lastData = "0";
    private volatile boolean recebeuDados = false;
    private final SerialPort arduinoPort;

    public ArduinoReaderThread(SerialPort porta) {
        this.arduinoPort = porta;
        setDaemon(true);
    }

    @Override
    public void run() {

        if (!arduinoPort.openPort()) {
            System.err.println("Erro: Nao foi possivel abrir a porta serial.");
            return;
        }

        arduinoPort.setBaudRate(9600);

        InputStream in = arduinoPort.getInputStream();
        StringBuilder currentLine = new StringBuilder();

        try {

            while (!Thread.currentThread().isInterrupted()) {

                if (in.available() > 0) {

                    char character = (char) in.read();
                    currentLine.append(character);

                    if (character == '\n') {

                        lastData = currentLine.toString().trim();
                        recebeuDados = true;
                        currentLine.setLength(0);
                    }
                }

                Thread.sleep(1);
            }

        } catch (Exception e) {

            System.err.println("Erro na leitura serial: " + e.getMessage());

        } finally {

            if (arduinoPort.isOpen()) {
                arduinoPort.closePort();
            }
        }
    }

    public String getLastData() {
        return lastData;
    }

    public boolean recebeuDados() {
        return recebeuDados;
    }

    public void stopReading() {
        this.interrupt();
    }
}