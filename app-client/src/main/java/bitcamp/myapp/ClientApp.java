package bitcamp.myapp;

import bitcamp.command.RouletteCommand;
import bitcamp.util.Prompt;

import bitcamp.vo.MP3Player;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) {
        ClientApp app = new ClientApp();
        app.execute();
    }

    void execute() {
        try {
            System.out.println("클라이언트 실행!");

            Socket socket = new Socket("localhost", 8888);

            PrintStream out = new PrintStream(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            // 플레이어 번호 수신
            String playerNumberMessage = in.nextLine();
            System.out.println(playerNumberMessage);

            // 닉네임 입력
            String nickname = Prompt.input("닉네임을 입력해 주세요(클라이언트): ");
            out.println(nickname);

            // 게임 시작 메시지 수신
            String startMessage = in.nextLine();
            System.out.println(startMessage);


            RouletteCommand rouletteCommand = new RouletteCommand();
            rouletteCommand.printRoulette();

            // 게임 진행
            while (true) {
                // 서버에서 메시지를 수신할 때까지 대기
                if (in.hasNextLine()) {
                    String gameMessage = in.nextLine();
                    System.out.println(gameMessage);

                    switch (gameMessage){
                        case "발사" :
                            MP3Player.shot();
                            break;
                        case "불발" :
                            MP3Player.emptyBullet();
                            break;
                        case "장전":
                            MP3Player.loadingBullet();
                            break;
                    }

                    // 서버가 게임 종료 메시지를 보내면 루프 종료
                    if (gameMessage.contains("게임 종료")) {
                        break;
                    }

                    // 서버가 차례를 기다리라는 메시지를 보내면
                    if (gameMessage.contains("차례입니다")) {
                        // 자신의 차례가 되었을 때 입력을 받음
                        printMenu();
                        String input = Prompt.input("입력: ");
                        out.println(input);

                        // 서버로부터의 응답을 수신
                        if (in.hasNextLine()) {
                            String serverResponse = in.nextLine();
                            System.out.println(serverResponse);
                        }
                    }
                } else {
                    // 데이터가 없을 경우 처리
                    System.out.println("서버로부터 더 이상의 데이터가 없습니다.");
                    break;
                }
            }

            in.close();
            out.close();
            socket.close();
            Prompt.close();
        } catch (Exception e) {
            System.out.println("클라이언트 오류입니다");
            e.printStackTrace();
        }
    }


    private void printMenu(){
        // 메뉴 옵션을 저장할 문자열 배열
        String[] options = {
            "1번 : 자신 쏘기",
            "2번 : 상대 쏘기"
        };

        // 화면에 표시할 최대 길이를 계산
        int maxLength = 0;
        for (String option : options) {
            if (option.length() > maxLength) {
                maxLength = option.length();
            }
        }

        // 테두리를 그릴 때 사용할 문자
        char borderChar = '*';

        // 테두리 출력
        printBorder(maxLength, borderChar);
        for (String option : options) {
            System.out.println(borderChar + " " + padRight(option, maxLength) + " " + borderChar);
        }
        printBorder(maxLength, borderChar);
    }

    // 문자열 오른쪽에 공백을 추가하여 길이를 맞춤
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    // 테두리 출력
    public static void printBorder(int length, char borderChar) {
        for (int i = 0; i < length + 9; i++) {
            System.out.print(borderChar);
        }
        System.out.println();
    }
}
