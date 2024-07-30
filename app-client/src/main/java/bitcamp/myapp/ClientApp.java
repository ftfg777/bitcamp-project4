package bitcamp.myapp;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {

        public static void main(String[] args) throws IOException {
            System.out.println("클라이언트 실행!");

            Scanner keyScan = new Scanner(System.in);

            Socket socket = new Socket("192.168.0.30", 8888);

            PrintStream out = new PrintStream(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

            while (true) {
                // 키보드 입력을 받아서 서버에게 전송한다.
                System.out.print("입력> ");
                String input = keyScan.nextLine();
                out.println(input);

                // 서버가 보낸 데이터를 수신한다.
                String str = in.nextLine();
                System.out.println(str);

                if (input.equals("quit")) {
                    break;
                }
            }

            in.close();
            out.close();
            socket.close();
            keyScan.close();
        }
}
