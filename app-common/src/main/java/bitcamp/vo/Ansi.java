package bitcamp.vo;

public class Ansi {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String REVERSED = "\u001B[7m";

    // Utility methods for colored text
    public static String coloredText(String text, String color) {
        return color + text + RESET;
    }

    public static String boldText(String text) {
        return BOLD + text + RESET;
    }

    public static String boldColoredText(String text, String color) {
        return BOLD + color + text + RESET;
    }

    public static void printlnWithColor(String text, String color) {
        System.out.println(coloredText(text, color));
    }

    public static void printWithColor(String text, String color) {
        System.out.print(coloredText(text, color));
    }

    private Ansi() {
        throw new UnsupportedOperationException("Utility class");
    }


}
