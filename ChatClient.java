package com.mycompany.chat.blocking;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Runnable {

    private final String SERVER_ADDRESS = "192.168.0.107";
    private ClientSocket clientSocket;
    private final Scanner scanner;

    public ChatClient() {
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {
            clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, ChatServer.PORT));
            System.out.println(
                    "Cliente conectado ao servidor em " + SERVER_ADDRESS + ":"
                            + ChatServer.PORT);
            new Thread(this).start();
            messageLoop();
        } finally {
            clientSocket.close();
        }
    }

    @Override
    public void run() {
        String msg;
        while ((msg = clientSocket.getMessage()) != null) {
            System.out.printf("msg recebida do servidor %s \n", msg);
        }
    }

    private void messageLoop() throws IOException {
        String msg;
        do {
            System.out.println("Digite uma mensagem (ou sair para finalizar)");
            msg = scanner.nextLine();
            clientSocket.sendMsg(msg);
        } while (!msg.equalsIgnoreCase("sair"));
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar o cliente " + ex.getMessage());
        }
        System.out.println("Cliente finalizado");
    }
}