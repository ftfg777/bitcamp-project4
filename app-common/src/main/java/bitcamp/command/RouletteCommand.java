package bitcamp.command;

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
        setTurn(INIT_SIZE);
        setTarget();   // 1~...size
    }




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
                if(shotNum<=turn){
                    str.append(shot[sbNum]);
                }
                //used shot
                else{
                    str.append(shot[sbNum]);
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


        str[0] = ( "          (_/-------------_______________________)\t");
        str[1] = ( "          `|  /~~~~~~~~~~\\                       |\t");
        str[2] = ( "           ;  |--------(-||______________________|\t");
        str[3] = ( "           ;  |--------(-| ____________|\t\t\t");
        str[4] = ( "           ;  \\__________/'\t\t\t\t\t\t\t");
        str[5] = ( "         _/__         ___;\t\t\t\t\t\t\t");
        str[6] = ( "      ,~~    |  __--~~\t\t\t\t\t\t\t\t");
        str[7] = ( "     '        ~~| (  |\t\t\t\t\t\t\t\t");
        str[8] = ( "    '      '~~  `____'\t\t\t\t\t\t\t\t");
        str[9] = ( "   '      '\t\t\t\t\t\t\t\t\t\t\t");
        str[10]= ("  '      `\t\t\t\t\t\t\t\t\t\t\t");
        str[11]= (" '       `\t\t\t\t\t\t\t\t\t\t\t");
        str[12]= ("'--------`\t\t\t\t\t\t\t\t\t\t\t");

        return str;
    }

    private String[] printOneShot(){
        String[] str= new String[13];

        str[0] = ( "⠀⠀⠀⢠⠔⠢⡄\t\t");
        str[1] = ( "⠀⠀⡰⠁⠀⠀⠈⢆\t\t");
        str[2] = ( "⠀⢰⠁⠀⠀⠀⠀⠈⡆\t");
        str[3] = ( "⠀⡇⠀⠀⠀⠀⠀⠀⢸ \t");
        str[4] = ( "⢰⠓⠒⠒⠒⠒⠒⠚⡆\t");
        str[5] = ( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[6] = ( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[7] = ( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[8] = ( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[9] = ( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[10]= ( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[11]= ( "⢨⣏⣉⣉⣉⣉⣉⣽⡅\t");
        str[12]= ( "⢸⣀⣀⣀⣀⣀⣀⣀⡇\t");

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

        setTurn(--turn);
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

    private void setTurn(int turn){
        this.turn = turn;
    }

    private void setTarget(){
        Random random = new Random();
        this.target = random.nextInt(INIT_SIZE); //0~...(INIT_SIZE-1)
    }

    public int getTarget(){
        return target;
    }



}
