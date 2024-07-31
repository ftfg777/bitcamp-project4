package bitcamp.myapp;

import bitcamp.command.RouletteCommand;
import bitcamp.vo.ClientHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerApp {

    private static final int MAX_PLAYERS = 2;
    private CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>(); //현재 연결된 클라이언트의 정보를 저장하는 리스트

    public static void main(String[] args) {
        RouletteCommand rouletteCommand = new RouletteCommand();
        rouletteCommand.excute();

        ServerApp server = new ServerApp();
        server.execute();
    }

    void execute()
    {
        try {
            System.out.println("서버 실행!");

            ServerSocket serverSocket = new ServerSocket(8888);

            while (clients.size() < MAX_PLAYERS) {
                Socket socket = serverSocket.accept();
                System.out.println("새 클라이언트 연결됨!");

                ClientHandler handler = new ClientHandler(socket, clients.size() + 1, clients);
                clients.add(handler);
                handler.start();
            }

            // 모든 클라이언트가 연결되면 게임 시작 메시지를 전송
            while (clients.get(1).getNickname()==null) {

            }
            startGame();
            // 계속 실행 상태 유지
            while (true) {
                // 게임 중 추가적인 서버 작업이 있을 경우 여기에 추가

            }
        }catch (Exception e)
        {
            System.out.println("서버 실행중 오류");
            e.printStackTrace();
        }
    }

    private void startGame() {
        String player1 = clients.get(0).getNickname();
        String player2 = clients.get(1).getNickname();
        for (ClientHandler client : clients) {
            client.sendMessage("게임 시작! " + player1 + "와 " + player2 + "가 진행합니다.");
        }
    }
}