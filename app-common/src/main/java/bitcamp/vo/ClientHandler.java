package bitcamp.vo;

import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler extends Thread{

    CopyOnWriteArrayList<ClientHandler> clients;

    private Socket socket;
    private int playerNumber;
    private String nickname;
    private PrintStream out;
    private Scanner in;

    public ClientHandler(Socket socket, int playerNumber, CopyOnWriteArrayList<ClientHandler> list) {
        this.socket = socket;
        this.playerNumber = playerNumber;
        clients = list;
    }

    @Override
    public void run() {
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new Scanner(socket.getInputStream());

            // 플레이어 번호 전송
            out.println("당신은 " + playerNumber + "번 플레이어입니다.");

            // 닉네임 요청 및 수신
            out.print("닉네임을 입력해 주세요: ");
            nickname = in.nextLine();
/*
            if (clients.size() == 2) {
                startGame();
            }
            */

            while (true) {
                // 클라이언트가 보낸 문자열을 수신한다.
                String str = in.nextLine();
                System.out.println("플레이어 " + playerNumber + "(" + nickname + "): " + str);

                if (str.equals("quit")) {
                    break;
                }

                // 다른 플레이어에게 메시지 전송
                for (ClientHandler client : clients) {
                    if (client != this) {
                        client.sendMessage(nickname + ": " + str);
                    }
                }
            }

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

    private void startGame() {
        String player1 = clients.get(0).getNickname();
        String player2 = clients.get(1).getNickname();
        for (ClientHandler client : clients) {
            client.sendMessage("게임 시작! " + player1 + "와 " + player2 + "가 진행합니다.");
        }
    }
}
