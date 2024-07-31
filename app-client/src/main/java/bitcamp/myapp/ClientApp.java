package bitcamp.myapp;

import bitcamp.command.RouletteCommand;
import bitcamp.util.Prompt;

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
            String nickname = Prompt.input("닉네임을 입력해 주세요: ");
            out.println(nickname);

            // 게임 시작 메시지 수신
            String startMessage = in.nextLine();
            System.out.println(startMessage);


            RouletteCommand rouletteCommand = new RouletteCommand();
            rouletteCommand.printRoulette();
            while (true) {
                String command = Prompt.input("번호를 입력해 주세용 : ");
                if (command.equals("quit")) {
                    out.println("quit");
                    break;
                }
//                out.println(command);

                // 서버가 보낸 데이터를 수신한다.
                String str = in.nextLine();
                System.out.println(str);
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
}
