package bitcamp.vo;

import bitcamp.command.RouletteCommand;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler extends Thread{

    CopyOnWriteArrayList<ClientHandler> clients;
    private static final Object lock = new Object(); //
    private ClientHandler currentTurnClient = null; // 현재 턴을 가진 클라이언트/ 동기화 객체
    private final RouletteCommand rouletteCommand;


    private Socket socket;
    private int playerNumber;
    private String nickname;
    private boolean death = false;
    private boolean turn;
    private PrintStream out;
    private Scanner in;

    public ClientHandler(Socket socket, int playerNumber, CopyOnWriteArrayList<ClientHandler> list, RouletteCommand rouletteCommand) {
        this.socket = socket;
        this.playerNumber = playerNumber;
        clients = list;
        this.rouletteCommand = rouletteCommand;

    }

    @Override
    public void run() {

        try {
            out = new PrintStream(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());

            if (playerNumber == 1)
            {
                this.turn = true;
            }else
            {
                this.turn = false;
            }

            // 플레이어 번호 전송
            out.println("당신은 " + playerNumber + "번 플레이어입니다.");

            nickname = in.nextLine();

            while (clients.get(1).getNickname() == null || clients.get(0).getNickname() == null) {

            }
            printStart();
            startGame();

            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("클라이언트 핸들링 오류");
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void printStart() {
            String player1 = clients.get(0).getNickname();
            String player2 = clients.get(1).getNickname();
            for (ClientHandler client : clients) {
                client.sendMessage("게임 시작! " + player1 + "와 " + player2 + "가 진행합니다.");
                break;
            }

    }

    private void startGame() {
        while (true) {
                for (ClientHandler client : clients) {
                    new Thread(() -> handleInput(client)).start();
                }
        }
    }

    private void handleInput(ClientHandler client) {
        try {
            while (true) {
                synchronized (lock) {
                    if (client.turn) {
                        System.out.println(rouletteCommand.getTurn() + "=========================");
                        client.sendMessage("당신의 차례입니다 입력을 시작하세요. 1번 : 자기자신 쏘기 | 2번 : 상대 쏘기");
                        client.sendMessage("차례입니다");
                        String message = client.in.nextLine();
                        int no = Integer.parseInt(message);
                        String resultMessage;

                        if (no == 1) {
                            resultMessage = "자기자신을 쏘았습니다";
                        } else if (no == 2) {
                            resultMessage = "상대를 쏘았습니다";
                        } else {
                            resultMessage = "없는 번호다 모자란 친구야";
                        }

                            rouletteCommand.excute();

                        broadcastMessage(client.nickname + ": " + resultMessage);

                        for (ClientHandler c : clients) {
                            if (c.turn) {
                                c.turn = false;
                            }else
                            {
                                c.turn = true;
                            }
                        }

                        lock.notifyAll(); // 모든 대기 스레드에 알림
                        break; // 종료

                    } else {
                        client.sendMessage("게임 시작! 상대의 턴 입니다 기다리세요");

                        lock.wait();// 자신의 턴이 아닐 때 대기
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("플레이어 입력 처리 중 오류 발생");
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private void resultMessage() {
        for (ClientHandler client : clients) {
            if (client.isDeath()){
                client.sendMessage(client.nickname +"가 패배했습니다.");
            }else{
                client.sendMessage(client.nickname +"가 승리했습니다.");
            }
        }
    }


    private boolean isDeath(){
        return this.death;
    }

}