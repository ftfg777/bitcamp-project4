package bitcamp.command;

import bitcamp.vo.Ansi;
import bitcamp.vo.Kor;

import java.util.Random;

public class RouletteCommand {
    /********************************************************************
     * 총 턴 횟수: 5(INIT_SIZE 고정)
     *
     * <<Constructor>>
     * 새 게임 진행 시 RouletteCommand 생성, turn=5, target random 세팅
     *
     * <<Method>>
     * 턴 진행마다 excute(), true(총알)/false(x) 반환
     * printRoulette()로 UI Print(턴 감소x)
     *
     *********************************************************************/

    //총 턴 횟수
    private final int INIT_SIZE = 5;

    //현재 턴( (INIT_SIZE-1)~...0 )
    private int turn;
    //총알 턴( 0~...(INIT_SIZE-1) )
    private int target;


    ///////////////////////////////////////////////////////////
    ////////////////////// Constructor ////////////////////////
    ///////////////////////////////////////////////////////////
    public RouletteCommand(){
        this.turn = INIT_SIZE;
        setTarget();   // 1~...size
    }

    ///////////////////////////////////////////////////////////
    ////////////////////// getInstance() //////////////////////
    ///////////////////////////////////////////////////////////
    private static RouletteCommand m;

    // setup Menu Instance
    public static RouletteCommand getInstance() {

        m = new RouletteCommand();

        return m;
    }// Method getInstance END

    // reset RouletteCommand Instance
    public static void freeInstance() {
        m = null;
    }// Method freeInstance END



    ///////////////////////////////////////////////////////////
    ////////////////////////// Method /////////////////////////
    ///////////////////////////////////////////////////////////
    //true  =>END
    //false =>continue
    public boolean excute(){
        printRoulette();
        return getResult();
    }

    public void printRoulette(){
        StringBuffer str = new StringBuffer("");

        String[] gun = printGun();
        String[] shot = printOneShot();

        //set TUI
        for(int sbNum = 0; sbNum<gun.length ; sbNum++){
            //one line gun
            str.append(gun[sbNum]);

            //ont line shots
            for(int shotNum =0; shotNum<INIT_SIZE; shotNum++) {
                //////////////////////////////////////////////
                // ANSI 이스케이프 시퀀스 적용 위치           //
                //////////////////////////////////////////////
                //unused shot
                if(shotNum<turn){
                    str.append(Ansi.boldColoredText(shot[sbNum], Ansi.WHITE));
                }
                //used shot
                if(shotNum==turn){
                    if((turn == target)){
                    str.append(Ansi.boldColoredText(shot[sbNum], Ansi.RED));
                    }
//                    else {
//                        str.append(Ansi.coloredText(shot[sbNum], Ansi.WHITE));
//                    str.append(shot[sbNum]);
//                    }
                }
                //////////////////////////////////////////////
                //////////////////////////////////////////////

            }
            //one line end
            str.append("\n");
            System.out.print(str);
            str.setLength(0);
        }
    }

    private String[] printGun(){
        String[] str=new String[13];


        str[0] = String.format("%-55s", "          (_/-------------_______________________)");
        str[1] = String.format("%-55s", "          `|  /~~~~~~~~~~\\                       |");
        str[2] = String.format("%-55s", "           ;  |--------(-||______________________|");
        str[3] = String.format("%-55s", "           ;  |--------(-| ____________|");
        str[4] = String.format("%-55s", "           ;  \\__________/'");
        str[5] = String.format("%-55s", "         _/__         ___;");
        str[6] = String.format("%-55s", "      ,~~    |  __--~~");
        str[7] = String.format("%-55s", "     '        ~~| (  |");
        str[8] = String.format("%-55s", "    '      '~~  `____'");
        str[9] = String.format("%-55s", "   '      '");
        str[10]= String.format("%-55s", "  '      `");
        str[11]= String.format("%-55s", " '      '");
        str[12]= String.format("%-55s", "--------");

        return str;
    }

    private String[] printOneShot(){
        String[] str= new String[13];

        str[0] = Kor.getWordFormat("%21s", "⠀    ⠔⠢⡄");
        str[1] = Kor.getWordFormat("%22s", "⠀⠀⡰⠁⠀⠀ ⢆        ");
        str[2] = Kor.getWordFormat("%23s", "⠀⢰⠀  ⠀⠀⠈⡆       ");
        str[3] = Kor.getWordFormat("%23s", " ⡇⠀⠀⠀⠀⠀⠀⢸       ");
        str[4] = Kor.getWordFormat("%23s", "⢰⠓⠒⠒⠒⠒⠒⠚⡆");
        str[5] = Kor.getWordFormat("%23s", "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇     ");
        str[6] = Kor.getWordFormat("%23s", "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇     ");
        str[7] = Kor.getWordFormat("%23s", "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇     ");
        str[8] = Kor.getWordFormat("%23s", "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇     ");
        str[9] = Kor.getWordFormat("%23s", "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇     ");
        str[10]= Kor.getWordFormat("%23s", "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇     ");
        str[11]= Kor.getWordFormat("%23s", "⢨⣏⣉⣉⣉⣉⣉⣽⡅");
        str[12]= Kor.getWordFormat("%23s", "⢸⣀⣀⣀⣀⣀⣀⣀⡇");

        return str;
    }

    public boolean getResult(){
        if(turn<0){
            System.out.println("DRAW"); //target's random error
            return true;
        }

        if(turn == target){
            System.out.println("BANG!!");
            return true;
        }

        setTurn();
        return false;
    }

    ///////////////////////////////////////////////////////////
    ///////////////// public getter, setter ///////////////////
    ///////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////
    //////////////////////////// -- ///////////////////////////
    //////////////////////////// -- ///////////////////////////
    //////////////////////////// -- ///////////////////////////
    //////////////////////// ---------- ///////////////////////
    ////////////////////////// ------ /////////////////////////
    //////////////////////////// -- ///////////////////////////
    ///////////////////////////////////////////////////////////

    public void setTurn(){
        this.turn = --turn;
    }

    private void setTarget(){
        Random random = new Random();
        this.target = random.nextInt(1, INIT_SIZE); //0~...(INIT_SIZE-1)
    }

    public int getTarget(){
        return target;
    }



    public String getTurn(){
        return String.valueOf(this.turn);
    }

}
