package bitcamp.vo;

import bitcamp.command.RouletteCommand;

import java.io.IOException;
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
    private final RouletteCommand rouletteCommand;
    private static boolean gameStarted = false; // 게임 시작 여부
    private volatile boolean flag = false;  // Volatile 키워드 추가


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

            synchronized (lock) {
                // 모든 클라이언트가 닉네임을 입력할 때까지 대기
                while (clients.stream().anyMatch(c -> c.getNickname() == null)) {
                    lock.wait();
                }

                if (!gameStarted) {
                    // 모든 클라이언트가 준비되었을 때 게임 시작 메시지 전송
                    printStart();
                    gameStarted = true; // 게임이 시작되었음을 기록
                }
            }
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
            broadcastMessage("게임 시작! " + player1 + "와 " + player2 + "가 진행합니다. ^^");
    }

    private void startGame() {
        while (!flag) {
                for (ClientHandler client : clients) {
                    new Thread(() -> handleInput(client)).start();
                }
        }
    }

    private void handleInput(ClientHandler client) {
        try {
            while (true) {
                synchronized (lock) {
                    if (flag) {
                        // flag가 true일 때 루프 종료
                        break;
                    }

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

                        broadcastMessage(client.nickname + "(이)가 " + resultMessage);
                        flag = rouletteCommand.excute();

                        if (flag) {
                            broadcastMessage("게임 종료");
                            lock.notifyAll(); // 대기중인 모든 스레드 알림
                            break;
                        }
                            // 턴 전환
                            for (ClientHandler c : clients) {
                                c.turn = !c.turn;
                            }

                        lock.notifyAll(); // 모든 대기 스레드에 알림
                        break; // 종료

                    } else {
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