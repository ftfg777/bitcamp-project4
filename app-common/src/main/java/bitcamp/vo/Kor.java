package bitcamp.vo;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;


//getWordFormat("%s", String);
public class Kor {

    private static final String WORD = "^\\S";
	
	public static String getWordFormat(String str, String data){
		int width = getWidth(str);
		return String.format(wordFormat(data, width), data);
	}


    //format(korean)
    private static String wordFormat(String data, int width){
        int cnt = getByteWord(data);
        String str ="";

        str = "%-"+ (width-cnt) +"s";
        return str;
    }

	//get Width
	private static int getWidth(String str){
		String[] str1 = stringSplitStart(str);
		String[] str2 = stringSplitEnd(str1[1]);
		return Integer.parseInt(str2[0]);
	}
	
	
	//Check Char Byte(korean)
    private static int getByteWord(String word){
        char[] arr = word.toCharArray();
        int cnt = 0;

        for(char w : arr){
            if(Pattern.matches(WORD, String.format("%c", w) ) ){
                cnt++;
            }
        }

        return cnt;
    }

    //Check String Byte
    private static int getByteString(String word){
        if(getType(word)) {
            try {
                return word.getBytes("euc-kr").length;
            } catch (UnsupportedEncodingException e) {
                return 0;
            }
        }
        return 0;
    }

    //get type(korean?)
    private static boolean getType(String word){
        return Pattern.matches(WORD, word);
    }
	
	//str Split %
	private static String[] stringSplitStart(String str){
        return str.split("%");
    }

	//str Split s
	private static String[] stringSplitEnd(String str){
        return str.split("s");
    }

}
