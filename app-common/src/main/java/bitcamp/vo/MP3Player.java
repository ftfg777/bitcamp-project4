package bitcamp.vo;

import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.IOException;

public class MP3Player {

    public static void shot() {
        ClassLoader classLoader = MP3Player.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("sound/shot.mp3")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("MP3 파일을 찾을 수 없습니다.");
            }
            Player player = new Player(inputStream);
            player.play();

        } catch (JavaLayerException | IOException e) {
            System.out.println("MP3 재생 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void emptyBullet() {
        ClassLoader classLoader = MP3Player.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("sound/emptyBullet.mp3")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("MP3 파일을 찾을 수 없습니다.");
            }
            Player player = new Player(inputStream);
            player.play();

        } catch (JavaLayerException | IOException e) {
            System.out.println("MP3 재생 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadingBullet() {
        ClassLoader classLoader = MP3Player.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("sound/loadingBullet.mp3")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("MP3 파일을 찾을 수 없습니다.");
            }
            Player player = new Player(inputStream);
            player.play();

        } catch (JavaLayerException | IOException e) {
            System.out.println("MP3 재생 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


