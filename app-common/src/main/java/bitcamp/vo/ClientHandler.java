package bitcamp.vo;

import bitcamp.command.RouletteCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
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
                    if (gameStarted){
                        new Thread(() -> handleInput(client)).start();
                    }
                }
        }
    }

    private void handleInput(ClientHandler client) {
        try {
             while (!flag) {
                synchronized (lock) {
                    if (!flag) {
                        if (client != null) {
                            if (client.turn) {
                                client.sendMessage("장전");
                                client.sendMessage("당신의 차례입니다 입력을 시작하세요.");

                                waitingMessage(client);

                                String message = client.in.nextLine();
                                int no = Integer.parseInt(message);
                                String resultMessage;

                                if (no == 1) {
                                    resultMessage = "자신을 쏘았습니다.";
                                } else if (no == 2) {
                                    resultMessage = "상대를 쏘았습니다.";
                                } else {
                                    resultMessage = "없는 번호다 모자란 친구야";
                                }

                                broadcastMessage(Ansi.boldColoredText(client.nickname, Ansi.PURPLE) + "(이)가 " + resultMessage);

                                flag = rouletteCommand.excute();

                                if (flag) {
                                    // 걸렸는데 자신을 쏜 경우
                                    if (no == 1) {
                                        client.death = true;
                                    }

                                    // 걸렸는데 상대방을 쏜 경우
                                    if (no == 2) {
                                        for (ClientHandler client2 : clients) {
                                            if (!client.getNickname().equals(client2.getNickname())) {
                                                client2.death = true;
                                                break;
                                            }
                                        }
                                    }

                                    client.sendMessage("발사");
                                    resultMessage();
                                    broadcastMessage("게임 종료");
                                    lock.notifyAll();
                                    gameStarted = false;
                                    break;
                                }


                                client.sendMessage("불발");
                                // 자신을 쏘고 살아남았을 경우
                                if ((!client.isDeath()) && no == 1) {
                                    broadcastMessage("\uD83C\uDF40 럭키비키! " + Ansi.coloredText(client.getNickname(), Ansi.PURPLE) + "님이 자신을 쏘고 살아남았습니다. 추가 기회를 획득합니다. \uD83C\uDF40");
                                    broadcastMessage(Ansi.boldColoredText(rouletteCommand.getTurn() + "발 남았습니다.", Ansi.RED));
                                    continue;
                                } else {
                                    broadcastMessage(Ansi.boldColoredText(rouletteCommand.getTurn() + "발 남았습니다.", Ansi.RED));
                                }

                                // 턴 전환
                                for (ClientHandler c : clients) {
                                    c.turn = !c.turn;
                                }

                                lock.notifyAll(); // 모든 대기 스레드에 알림
                                break; // 종료

                            } else {
                                lock.wait();
                            }
                        }
                    }else
                    {
                        break;
                    }
                }
            }
        } catch (IllegalStateException e) {
            flag = true;
            System.exit(0);
            System.out.println("IllegalStateException 오류 발생");
        }catch (NoSuchElementException e){
            flag = true;
            System.exit(0);
            System.out.println("NoSuchElementException 오류 발생");
        }
        catch (Exception e)
        {
            System.out.println("플레이어 입력 처리 중 오류 발생");
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private void waitingMessage(ClientHandler client){
        for (ClientHandler client2 : clients) {
            if (!client.getNickname().equals(client2.getNickname())) {
                client2.sendMessage("상대(" + Ansi.boldColoredText(client.getNickname(), Ansi.PURPLE) + ") 턴입니다.");
                break;
            }
        }
    }

    private void resultMessage() {
        for (ClientHandler client : clients) {
            if (client.isDeath()){
                client.sendMessage(Ansi.boldText("===================================================="));
                client.sendMessage("☠️" + client.nickname +"(이)가 패배했습니다." + "☠️");
                client.sendMessage(Ansi.boldText("===================================================="));
            }else{
                client.sendMessage(Ansi.boldText("===================================================="));
                client.sendMessage("\uD83C\uDF89" + client.nickname +"(이)가 승리했습니다." + "\uD83C\uDF89");
                client.sendMessage(Ansi.boldText("===================================================="));
            }
        }
    }

    private boolean isDeath(){
        return this.death;
    }

}