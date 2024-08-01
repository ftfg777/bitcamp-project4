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
    private static RouletteCommand rouletteCommand = new RouletteCommand();
    public static void main(String[] args) {

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

                ClientHandler handler = new ClientHandler(socket, clients.size() + 1, clients, rouletteCommand);
                clients.add(handler);
                handler.start();
            }

        }catch (Exception e) {
            System.out.println("서버 실행중 오류");
            e.printStackTrace();
        }
    }
}