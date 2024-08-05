package bitcamp.vo;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.IOException;

public class MP3Player {

    public static void shot() {
        String filePath = "app-common/src/main/resources/sound/shot.mp3";
//        String filePath = "app-common/src/main/resources/sound/shot.mp3";
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Player player = new Player(fileInputStream);
            player.play();

        } catch (JavaLayerException | IOException e) {
            System.out.println("MP3 재생 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void emptyBullet() {
        String filePath = "app-common/src/main/resources/sound/emptyBullet.mp3";
//        String filePath = "app-common/src/main/resources/sound/emptyBullet.mp3";
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Player player = new Player(fileInputStream);
            player.play();

        } catch (JavaLayerException | IOException e) {
            System.out.println("MP3 재생 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadingBullet() {
        String filePath = "app-common/src/main/resources/sound/loadingBullet.mp3";
//        String filePath = "app-common/src/main/resources/sound/loadingBullet.mp3";
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Player player = new Player(fileInputStream);
            player.play();

        } catch (JavaLayerException | IOException e) {
            System.out.println("MP3 재생 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


