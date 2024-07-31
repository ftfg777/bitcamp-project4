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
    RouletteCommand(){
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
        System.out.print(printShots());
    }

    private StringBuffer printShots(){
        StringBuffer str = new StringBuffer("");

        StringBuffer[] gun = printGun();
        StringBuffer[] shot = printOneShot();

        //set TUI
        for(int sbNum = 0; sbNum<gun.length ; sbNum++){
            //one line gun
            str.append(gun[sbNum]);

            //ont line shots
            for(int shotNum =0; shotNum<INIT_SIZE; shotNum--) {
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
        }

        return str;
    }

    private StringBuffer[] printGun(){
        StringBuffer[] str=new StringBuffer[14];

        str[0].append( "          (_/-------------_______________________)\t");
        str[1].append( "          `|  /~~~~~~~~~~\\                       |\t");
        str[2].append( "           ;  |--------(-||______________________|\t");
        str[3].append( "           ;  |--------(-| ____________|\t\t\t");
        str[4].append( "           ;  \\__________/'\t\t\t\t\t\t\t");
        str[5].append( "         _/__         ___;\t\t\t\t\t\t\t");
        str[6].append( "      ,~~    |  __--~~\t\t\t\t\t\t\t\t");
        str[7].append( "     '        ~~| (  |\t\t\t\t\t\t\t\t");
        str[8].append( "    '      '~~  `____'\t\t\t\t\t\t\t\t");
        str[9].append( "   '      '\t\t\t\t\t\t\t\t\t\t\t");
        str[10].append("  '      `\t\t\t\t\t\t\t\t\t\t\t");
        str[11].append(" '       `\t\t\t\t\t\t\t\t\t\t\t");
        str[12].append("'--------`\t\t\t\t\t\t\t\t\t\t\t");

        return str;
    }

    private StringBuffer[] printOneShot(){
        StringBuffer[] str= new StringBuffer[14];

        str[0].append( "⠀⠀⠀⢠⠔⠢⡄\t\t");
        str[1].append( "⠀⠀⡰⠁⠀⠀⠈⢆\t\t");
        str[2].append( "⠀⢰⠁⠀⠀⠀⠀⠈⡆\t\t");
        str[3].append( "⠀⡇⠀⠀⠀⠀⠀⠀⢸\t\t");
        str[4].append( "⢰⠓⠒⠒⠒⠒⠒⠚⡆\t");
        str[5].append( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[6].append( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[7].append( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[8].append( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[9].append( "⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[10].append("⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇\t");
        str[11].append("⢨⣏⣉⣉⣉⣉⣉⣽⡅\t");
        str[12].append("⢸⣀⣀⣀⣀⣀⣀⣀⡇\t");

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
