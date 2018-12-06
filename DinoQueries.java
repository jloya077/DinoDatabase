import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

/* This class contains modification statements of admin, UI ASCII art for the terminal, and queries used across the program. */
public class DinoQueries
{
    protected static Boolean colorMode = false;

    protected static String dinoBanner2 = "██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗" + "\n" +  //source http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20
                                          "██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗" + "\n" +
                                          "██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║" + "\n" +
                                          "██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║" + "\n" +
                                          "╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝" + "\n" +
                                           "╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝"; 
                                                                                        
    protected static String dinoBanner = "██████╗ ██╗███╗   ██╗ ██████╗ ██████╗  █████╗ ████████╗ █████╗ ██████╗  █████╗ ███████╗███████╗" + "\n" +
                                         "██╔══██╗██║████╗  ██║██╔═══██╗██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗██╔══██╗██╔══██╗██╔════╝██╔════╝" + "\n" +
                                         "██║  ██║██║██╔██╗ ██║██║   ██║██║  ██║███████║   ██║   ███████║██████╔╝███████║███████╗█████╗" + "\n" +  
                                         "██║  ██║██║██║╚██╗██║██║   ██║██║  ██║██╔══██║   ██║   ██╔══██║██╔══██╗██╔══██║╚════██║██╔══╝" + "\n" +  
                                         "██████╔╝██║██║ ╚████║╚██████╔╝██████╔╝██║  ██║   ██║   ██║  ██║██████╔╝██║  ██║███████║███████╗" + "\n" +
                                         "╚═════╝ ╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚══════╝";
                                                                                                   

    protected static String jurassicDino =   
    "$$$$$$$$$$$$$$$$$$$$$$$$$$$**'''`` ````'''#*R$$$$$$$$$$$$$$$$$$$$$$$$$$$" + "\n" + //SOURCE http://www.asciiworld.com/-Dinosaurs-.html
    "$$$$$$$$$$$$$$$$$$$$$$*''      ..........      `'#$$$$$$$$$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$$$$$#''    .ue@$$$********$$$$Weu.   `'*$$$$$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$$#''   ue$$*#''              `''*$$No.   'R$$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$P'   u@$*'`                         '#$$o.  ^*$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$P'  .o$R'               . .WN.           '#$Nu  `#$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$'  .@$#`       'ou  .oeW$$$$$$$$W            '$$u  '$$$$$$$$$$$" + "\n" +
    "$$$$$$$$#   o$#`      ueL  $$$$$$$$$$$$$$$$ku.           '$$u  '$$$$$$$$$" + "\n" +
    "$$$$$$$'  x$P`        `'$$u$$$$$$$$$$$$$$'#$$$L            '$o   *$$$$$$$" + "\n" +
    "$$$$$$'  d$'        #$u.2$$$$$$$$$$$$$$$$  #$$$Nu            $$.  #$$$$$$" + "\n" +
    "$$$$$'  @$'          $$$$$$$$$$$$$$$$$$$$k  $$#*$$u           #$L  #$$$$$" + "\n" +
    "$$$$'  d$         #Nu@$$$$$$$$$$$$$$$$$$'  x$$L #$$$o.         #$c  #$$$$" + "\n" +
    "$$$F  d$          .$$$$$$$$$$$$$$$$$$$$N  d$$$$  '$$$$$u        #$L  #$$$" + "\n" +
    "$$$  :$F        ..`$$$$$$$$$$$$$$$$$$$$$$$$$$$`    R$$$$$eu.     $$   $$$" + "\n" +
    "$$!  $$        . R$$$$$$$$$$$$$$$$$$$$$$$$$$$$$.   @$$$$$$$$Nu   '$N  `$$" + "\n" +
    "$$  x$'        Re$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$uu@'``'$$$$$$$i   #$:  $$" + "\n" +
    "$E  $$       c 8$$$$$$$$$$$$$$$$$$$$$G(   ``^*$$$$$$$WW$$$$$$$$N   $$  4$" + "\n" +
    "$~ :$$N. tL i)$$$$$$$$$$$$$$$$$$$$$$$$$N       ^#R$$$$$$$$$$$$$$$  9$  '$" + "\n" +
    "$  t$$$$u$$W$$$$$$$$$$$$$$!$$$$$$$$$$$$$&       . c?'*$$$R$$$$$$$  '$k  $" + "\n" +
    "$  @$$$$$$$$$$$$$$$$$$$$'E F!$$$$$$$$$$.'        +.'@'' x .''*$$'   $B  $" + "\n" +
    "$  $$$$$$$$$$$$$$$$'$)#F     $$$$$$$$$$$           `  -d>x'*=.'`    $$  $" + "\n" +
    "$  $$$$$$$$$$?$$R'$ `#d$''    #$$$$$$$$$ > .                ''       $$  $" + "\n" +
    "$  $$$$$$$($$@$'` P *@$.@#'!    '*$$$$$$$L!.                        $$  $" + "\n" +
    "$  9$$$$$$$L#$L  ! '' <$$`          '*$$$$$NL:'z  f                  $E  $" + "\n" +
    "$> ?$$$$ $$$b$^      .$c .ueu.        `'$$$$b'x'#  '               x$!  $" + "\n" +
    "$k  $$$$N$ '$$L:$oud$$$` d$ .u.         '$$$$$o.'' #f.              $$   $" + "\n" +
    "$$  R$'''$$o.$'$$$$'''' ue$$$P'`'c          '$$$$$$Wo'              :$F  t$" + "\n" +
    "$$: '$&  $*$$u$$$$u.ud$R' `    ^            '#*****               @$   $$" + "\n" +
    "$$N  #$: E 3$$$$$$$$$'                                           d$''  x$$" + "\n" +
    "$$$k  $$   F *$$$$*''                                            :$P   $$$" + "\n" +
    "$$$$  '$b                                                      .$P   $$$$" + "\n" +
    "$$$$b  `$b                                                    .$$   @$$$$" + "\n" +
    "$$$$$N  '$N                                                  .$P   @$$$$$" + "\n" +
    "$$$$$$N  '*$c                                               u$#  .$$$$$$$" + "\n" +
    "$$$$$$$$.  '$N.                                           .@$'  x$$$$$$$$" + "\n" +
    "$$$$$$$$$o   #$N.                                       .@$#  .@$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$u  `#$Nu                                   u@$#   u$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$u   'R$o.                             ue$R'   u$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$o.  ^#$$bu.                     .uW$P'`  .u$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$$$$u   `'#R$$Wou..... ....uueW$$*#'   .u@$$$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$$$$$$Nu.    `'''#***$$$$$***'''`    .o$$$$$$$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$$$$$$$$$$$eu..               ...ed$$$$$$$$$$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$NWWeeeeedW@$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + "\n" +
    "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$";


protected static String jurassicDino2 = //SOURCE https://manytools.org/hacker-tools/convert-images-to-ascii-art/
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&&&&&&&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%%%%%%%%%%%%%%%%%%%%%%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%%%&@@@@@@@@@@@@@@@@@@@@@@@&%%%%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%%%@@@@@@&/*******************/&@@@@@@%%%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%@@@@@#**********************@@*******#@@@@@%%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%%@@@@%**************&&/**(@@@@@@@@@@@********#@@@@%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%&@@@#**************%@%*#@@@@@@@@@@@@@@@@@/********(@@@&%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%&@@@*******************@@@@@@@@@@@@@@@&**@@@@/*********@@@@%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%&@@&*****************#@@@@@@@@@@@@@@@@@@@&**@@%@@@*********&@@@%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%@@@*****************@%*@@@@@@@@@@@@@@@@@@***&@@**&@@@@********&@@&%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%@@@********************@@@@@@@@@@@@@@@@@@@@%/@@@@(***&@@@@@*******@@@%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%@@@******************/%*@@@@@@@@@@@@@@@@@@@@@@@@@@(****@@@@@@@@@****%@@%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%@@(******************#%(@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#@@***@@@@@@@@***/@@&%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%@@/********************@@@@@@@@@@@@@@@@@@@@@%******(@@@@@@@@@@@@@@@@@/***@@&%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%@@/************/***%*%&@@@@@@@@@@@@@@@@@@@@@@@@@******(/*&@@@@@@@@@@@@@****@@&%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%@@%*******(**@%*&@%@@@@@@@@@@@@@@@@(@%@@@@@@@@@%/%*****/*&@/@@/&**//@@@*****/@@%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%@@@****@@&/@@@@@@@@@@@@@@@@@@@@@@@/&*/*@@@@@@@@@@*(********/*/((&%(#/@********&@@%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%@@@@&*/@@@@@@@@@@@@@@@@@@@@@&@*&&&*/*****@@@@@@@@@//#**************************@@&%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%@@@@@@@@@@@@@@@@@@@@@@@@@(@#*/@*&****&@%*****@@@@@@@&(*@***&********************%@@%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%@@@@@@@@@@@@@@@@@@@@@@@&*@@**&*/***#@*@@@#*******@@@@@&%#%@**/*******************@@%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%@@@@@@@@@@@@@@@@@@@@@@@@&%&*********@@*************%@@@@@/(@#(%******************@@&%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%&@@@@@@@@@@@@@@@@@@@@@%*@@@@**@***#@@@(*@@@&**********(@@@@@@@********************&@@%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%&@@@@@@@@@@@@@@@@@@@@@@@@*/@@@@@@&//**#@@(@@&*************************************&@@%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&&&&&&&&&&&&&&&&%%&@@@@@@@@@@@@@@*@&**@**/@@@%&@@@(%%&@@@*#****@************************************&@@%%&%&&&%&&&&&&&&&&@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@%%%&&&&&&&&&&&&&&&&@@@@@@@@@@@%(@@(@((#@(((%(@@@@@@@@(((((((((((((((((((((((((((((((((((((((((((((((@@@&&&&&&&&&&&&&&&%%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@..(..*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&@@@@@@%@@@@@@@@@@@@@@@@@@&..**%,..@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@..(..((.#.(@@/.(.&./,/#..#@@@&.#%#.(@@@..,./@(.,..@.,.*@@@,...@@@@@..,./..%.,@@#.***.%@@.**/%,.#@@.(.*@&...%@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@,./..%&././@@(.(.@.*.%*.,,.@@,....,.@&..,.(@..#..@@.,.(@&.,,.#@@@@@..,.@@/.%.*@......,@@.*.#*.,,.@./.*@.*.,@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@(.,..&@.*.*@@#.(.@.*.%@@.#.,@.#.@.(.@,.%./@@....@@@...#@./.,@@@@@@@..,.@@@./..@.#.@.#.@@.,.#@@.(.*.*.,.*.*@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@%.,..&@.*./@@%.#.@.*.%@,.%.%%...@/..#@..*.%@%.(..@@...#*.,.@@@@@@@@..,.@@@.(..#..,@*..%@.,.#@,.%.%.,.././@@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@&....@@.,.(@@#.#.@.*....#.@@.(.%@@.(.@@/.#..@@.,..@...#....@@@@@@@@..*.@@../.#.(.%@@./.@.,....#.@@.,.#./.@@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@&....@@.,.%@@(.#.@.*#,./.*@@.(,,.,,#.@@@....&@(.#.,.,.((.#.&@@@@@@@..*.../..%@.(,,,,,#.@.,#*./.,@@.,..,./.@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@&..,.@@....,*..#.@./.,/.#.@.*........,@*.#.*@%.#..@.,./@..#./@@@@@@..*,...#@@,/..,,....,.*.,%.%.&@.,.,@,.(.@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@.*,..,..@@*../#(..#.@.(.,@*.,..#.%@@@/.(..,,.&,../.*@@.,.,@@,.,(..%@@@..,.@@@@@@.%.#@@@/.#..*..@(./.@.,..@@..(.&@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@..%%*...@@@@@#..*#@,,,*@@,((,,,@@@@#...%&.@@@@@.@@@@,,,*@@@@(.@@@@@@..,.&@@@@(,,,@@@@#....,,,@@/.#@***,@@@,@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@,.....&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&....&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@&%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%@@@#**********(&@******%*//(*****(%/***#************@@@@%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%@@@@(*/@@///&@&@(@@@&*@@@%%*@@@@&(@@#/*******&@@@&%%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%%@@@@@@%@@%%*/*@&@@#@@&@@@@@@@(@@/@@@(@#@@@@@@&%%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%%%%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%%%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%%%%%%%%&@@@@@@@@@@@@@@&%%%%%%%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&%%%%%%%%%%%%%%%%%%%%%%&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + "\n" +
"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";




    //Terminal Colors Start
    public static final String ANSI_BLUE = "\u001B[34m"; //SOURCE https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println/5762502#5762502
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    //Terminal Colors End

    //UI Stuff Start
    protected static String MainMenu1, MainMenu2, Dino, DinoMenu1, DinoMenu2, UserMenu1, UserMenu2, uiDivider = ""; 
    protected static String colorBanner2 = ANSI_RED_BACKGROUND + ANSI_YELLOW + dinoBanner2 + ANSI_RESET;
    protected static String colorBanner = ANSI_RED_BACKGROUND + ANSI_YELLOW + dinoBanner + ANSI_RESET;
    protected static String colorDino = ANSI_RED_BACKGROUND + ANSI_YELLOW + jurassicDino + ANSI_RESET;
    protected static String colorDino2 = ANSI_RED_BACKGROUND + ANSI_YELLOW + jurassicDino2 + ANSI_RESET;
    protected static String colorDivDinoDB = ANSI_RED_BACKGROUND + ANSI_YELLOW + "-------DinoDatabase Main Menu--------" + ANSI_RESET;
    protected static String colorUserMenu = ANSI_RED_BACKGROUND + ANSI_YELLOW + "-------User Menu--------" + ANSI_RESET;
    protected static String colorUserMenuDiv = ANSI_RED_BACKGROUND + ANSI_YELLOW + "------------------------" + ANSI_RESET;
    protected static String colorDivDB = ANSI_RED_BACKGROUND + ANSI_YELLOW + "-------------------------------------" + ANSI_RESET;
    protected static String DivDinoDB = "-------DinoDatabase Main Menu--------";
    protected static String UserMenu = "-------User Menu--------";
    protected static String UserMenuDiv =  "------------------------";
    protected static String DivDB =  "-------------------------------------" ;
    protected static String colorgenDiv = ANSI_RED_BACKGROUND + ANSI_YELLOW + "--------------------------------------------------------------------------------------------" + ANSI_RESET;
    protected static String genDiv = "--------------------------------------------------------------------------------------------";

    //UI Stuff End

    //Queries used userLogin
    protected static String logCheck = "select * from userbase where u_username = ? and u_password = ?;"; //1
    protected static String logCheck2 = "select u_username from userbase where u_username = ?"; //22 (again maybe?)
    protected static String logCreate = "insert into userbase values(?,?,?);";
    
    //Queries Used UserTableInfo
    protected static String selFossil = "select d_name, f_fossilData, f_fossilEvidence, f_period from Dinosaur, fossil where d_dinokey = f_dinokey"; //2
    protected static String selDino = "select * from Dinosaur"; //3
    protected static String selFossil2 = "select * from fossil";
    protected static String selPhysTrait = "select * from physicalTraits"; 
    protected static String selPronounce = "select * from pronunciation";
    protected static String selTax = "select * from taxonomy";
    protected static String selTime = "select tp_comment, tp_name from timeperiod"; //4
    protected static String selRequest =  "select * from requests";
    protected static String selRequestSpec = "select * from requests where r_name = ? and r_updatestatus = 'f'"; 
    //UserTableInfo Queries End

    //Queries UserQuery1
    protected static String maxDinoKey = "select max(d_dinokey) from Dinosaur"; //5
    //Queries UserQuery1 End

    //Queries UserQuery3
    protected static String dinoCheck = "select d_name, d_dinokey from dinosaur where d_name = ?"; //6
    protected static String findDino = "select d_name, l_nation, h_name, tp_yearsAgo " + 
                                       "from Dinosaur, location, habitat, timeperiod " + 
                                       "where l_dinokey like ? and d_dinokey = ? and h_key = d_habkey and d_timeperiod = tp_name and d_name = ?;"; //7
    //Queries UserQuery3 End

    //Queries UserQuery4
    protected static String findSpecies = "select d_name from Dinosaur, taxonomy where  t_species = ? and d_name = t_genus;"; //8
    //Queries UserQuery4 End

    //Queries UserQuery5
    protected static String longDinos = "SELECT h_name ,d_name , MAX(pt_length) FROM Dinosaur, physicalTraits, habitat " + //9
                                        "WHERE d_dinokey = pt_dinokey AND h_key = d_habkey " + 
                                        "GROUP BY h_name " +
                                        "ORDER BY pt_length ASC;";
    //Queries UserQuery5 End

    //Queries UserQuery6
    protected static String topHeavy = "SELECT h_name ,d_name , MAX(pt_weight) " + //10
                                        "FROM Dinosaur, physicalTraits, habitat " +
                                        "WHERE d_dinokey = pt_dinokey AND h_key = d_habkey and pt_weight != 'unknown' " +
                                        "GROUP BY d_name " +
                                        "ORDER BY pt_weight desc " +
                                        "LIMIT ?;";
    //Queries UserQuery6 End

    //Queries UserQuery7
    protected static String minHeight = "SELECT d_name , pt_height " + //11
                                        "FROM Dinosaur, physicalTraits " +
                                        "WHERE d_dinokey = pt_dinokey and pt_height != 'unknown' " +
                                        "GROUP BY d_name " +
                                        "HAVING pt_height >= ?;";
    //Queries UserQuery7 End

    //Queries UserQuery8
    protected static String numDinosHabDiet = "SELECT COUNT(d_name) " + //12
                                              "FROM Dinosaur " +
                                              "WHERE d_name IN (SELECT d_name FROM Dinosaur, habitat " +
                                              "WHERE d_habkey = h_key " +
                                              "AND h_name = ? AND d_diet = ?);";
    //Queries UserQuery8 End

    //Queries UserQuery9
    protected static String numDinosType = "SELECT COUNT(*) " + //13
                                            "FROM (SELECT d_name as dName " +
                                            "FROM Dinosaur, physicalTraits " +
                                            "WHERE d_type like ? AND d_dinokey = pt_dinokey " +
                                            "GROUP BY d_name) as SQ1";
    //Queries UserQuery9 End

    //Queries UserQuery10
    protected static String longestDino = "SELECT p_name ,p_enunciation, SQ1.maxL " + //14
                                          "FROM pronunciation , Dinosaur, (SELECT d_dinokey as maxDino, max(pt_length) as maxL " +
                                          "FROM Dinosaur, physicalTraits " +
                                          "WHERE pt_dinokey = d_dinokey) as SQ1 " +
                                          "WHERE p_name = d_name AND d_dinokey = SQ1.maxDino;";
    //Queries UserQuery10 End

    //Queries UserQuery11
    protected static String dinoLength = "SELECT d_name, pt_length " + //15
                                         "FROM Dinosaur, physicalTraits " +
                                         "WHERE d_dinokey = pt_dinokey AND pt_length BETWEEN ? AND ?;";
    
    protected static String dinoHeight = "SELECT d_name, pt_height " +
                                         "FROM Dinosaur, physicalTraits " +
                                         "WHERE d_dinokey = pt_dinokey AND pt_height BETWEEN ? AND ?;";
    
    protected static String dinoWeight = "SELECT d_name, pt_weight " +
                                         "FROM Dinosaur, physicalTraits " +
                                         "WHERE d_dinokey = pt_dinokey AND pt_weight BETWEEN ? AND ?;";
    //Queries UserQuery11 End

    //Queries UserQuery12
    protected static String speciesHab = "SELECT d_name, t_species " + //16
                                         "FROM taxonomy, Dinosaur " +
                                         "WHERE t_dinokey = d_dinokey and d_habkey IN (SELECT h_key FROM habitat WHERE h_name = ?);";
    protected static String findHab = "select h_name from habitat where h_name = ?";
    protected static String findDiet = "select d_diet from dinosaur where d_diet = ?";
    //Queries UserQuery12 End

    //Queries UserQuery13
    protected static String dinoBodyType = "select d_name, pt_body, pt_length, tp_name " + //17
                                           "from Dinosaur, timeperiod, physicalTraits " +
                                           "where d_timeperiod = tp_name and d_dinokey = pt_dinokey " +
                                           "and pt_body like ?;";
    //Queries UserQuery13 End

    //Queries UserQuery14
    protected static String dinoMvmtMouthDiet = "select d_name, d_type, d_diet, pt_mouth, tp_name " + //18
                                                "from Dinosaur, habitat, timeperiod, physicalTraits " +
                                                "where d_habkey = h_key and d_type = ? " +
                                                "INTERSECT " +
                                                "select d_name, d_type, d_diet, pt_mouth, tp_name " +
                                                "from Dinosaur, fossil, timeperiod, physicalTraits " +
                                                "where  d_timeperiod = tp_name and f_dinokey = d_dinokey " +
                                                "and pt_mouth like ? and d_name = pt_name " +
                                                "group by d_name " + 
                                                "having d_diet = ?;";
    //Queries UserQuery14 End

    //Queries HistQuery1
    protected static String reqInsert = "insert into requests values(?, ?, ?, ?, ?, ?, ?)";
    protected static String reqMaxKey = "select max(r_requestkey) from requests;"; //24?
    //Queries HistQuery1 End

    //Queries FindHist
    protected static String reqFind = "select * from requests where r_username = ?"; //21


    //Queries deleteInfo
    protected static String delDino = "delete from Dinosaur where d_name = ?;";
    protected static String findDinoLoc = "select l_dinokey, l_nation " + //19
                                          "from location " +
                                          "where l_dinokey like ? ";

    protected static String updateDinoLoc = "update location set l_dinokey = ? where l_nation = ?;";
    //Queries deleteInfo End

     

    //Queries insertData
    protected static String insertDino = "insert into Dinosaur values(?, ?, ?, ?, ?, ?, ?)";
    protected static String insertFossil = "insert into fossil values(?, ?, ?, ?);";
    protected static String insertPhysTrait = "insert into physicalTraits values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    protected static String insertPronounce = "insert into pronunciation values(?, ?, ?);";
    protected static String insertTax = "insert into taxonomy values(?, ?, ?, ?, ?);";
    protected static String findDinoLoc2 = "select * from location where l_nation = ?"; //20 (maybe? might be a stretch)
    //Queries insertData End

     //Arrays Used InsertInfo
     protected static String tables[]  = new String[]{selDino, selFossil2, selPhysTrait, selPronounce, selTax};
     protected static String entries[] = new String[]{insertDino, insertFossil, insertPhysTrait, insertPronounce, insertTax,};
     //Arrays End

    //Queries UpdateData
    protected static String[] tableNames = new String[]{"Dinosaur", "fossil", "physicalTraits", "pronunciation", "taxonomy", "location"};
    //Queries UpdateData End

    //Queries UpdateUser
    protected static String selUser = "select u_username, u_type from userbase;"; //23?
    protected static String updateStatus = "update userbase set u_type = ? where u_username = ?";
    //Queries UpdateUser End

    //Queries Update Request
    protected static String updateReq = "update requests set r_updatestatus = 't' where r_requestkey = ?";
    //Queries Update Request End

    //Queries Delete Request
    protected static String delReq = "delete from requests where r_updatestatus = 't';";
    //Queries Delete Request End

    public static String formatString(String in) //formats String so that the input is always right no matter how the user types it
    { 
        if(in != null && in.length() > 0)
        {
            in = in.toLowerCase();
            in = in.substring(0,1).toUpperCase() + in.substring(1);
        }
        return in;

    }
    public static void deleteData(Connection conn, Scanner input) throws SQLException
  {
    ResultSet result = null;
    PreparedStatement pre = null;
    String quickFix = input.nextLine();
    String dinoName, uSure, dinoKey, preStmt = "";

    System.out.print("Please Enter Dinosaur to delete: ");
    dinoName = input.nextLine();
    dinoName = formatString(dinoName);
    System.out.println();

    
    preStmt = dinoCheck;
    pre = conn.prepareStatement(preStmt);
    pre.setString(1, dinoName);
    result = pre.executeQuery();
    if(!result.next())
    {
        System.out.println("Dinosaur not in database.");
        return;
    }
    else
        dinoKey = result.getString("d_dinokey");

    System.out.println("Are you sure you want to delete " + dinoName + " from database");
    System.out.println("Enter 'Yes' or 'No' ");
    uSure = input.nextLine();
    uSure = uSure.toLowerCase();

    if(uSure.equals("yes"))
    {
      preStmt = delDino;
      pre = conn.prepareStatement(preStmt);
      pre.setString(1, dinoName);
      pre.executeUpdate();

      preStmt = findDinoLoc;
      
      pre = conn.prepareStatement(preStmt);
      pre.setString(1, "%" + dinoKey + "%");
      result = pre.executeQuery();
      while(result.next())
      {
        String loc = result.getString(1);
        String locNation = result.getString(2);
        dinoKey = dinoKey + ",";
        int locIndex = loc.indexOf(dinoKey);
        loc = loc.substring(0, locIndex) + loc.substring(locIndex + dinoKey.length());

        preStmt = updateDinoLoc;
        pre = conn.prepareStatement(preStmt);
        pre.setString(1, loc);
        pre.setString(2, locNation);
        pre.executeUpdate();
      }
      
      System.out.println(dinoName + " deleted.");
      
    }
    else if(uSure.equals("no"))
      System.out.println("Delete Operation Cancelled.");
    else 
      System.out.println("Invalid Input");
    
    pre.close();
    result.close();
    return;
  
  }
    public static void insertData(Connection conn, Scanner input) throws SQLException
    {  
        ResultSet result = null;
        PreparedStatement pre = null;
        Statement stmt = conn.createStatement();

        String quickFix = input.nextLine();
        String colName, dinoName, info = "";
        String dinoKey = "";
        int maxKey, counter = 0;

        System.out.print("Enter Dinosaur You'd Like to insert: ");
        dinoName = input.nextLine();
        dinoName = formatString(dinoName);
        System.out.println();


        pre = conn.prepareStatement(dinoCheck);
        pre.setString(1, dinoName);
        result = pre.executeQuery();
        if(result.next())
        {
            System.out.println("Dinosaur already in database.");
            return;
        }

        result = stmt.executeQuery(maxDinoKey);
        if(result.next())
        {
            maxKey = result.getInt(1);
            maxKey++;
            dinoKey = Integer.toString(maxKey);
        }

        while(counter < tables.length)
        {
            result = stmt.executeQuery(tables[counter]);
            ResultSetMetaData resultMeta = result.getMetaData();
            int colNumber = resultMeta.getColumnCount();
            pre = conn.prepareStatement(entries[counter]);
            System.out.println(uiDivider);
            for(int i = 1; i <= colNumber; i++)
            {
                colName = resultMeta.getColumnName(i);
                System.out.print("Enter " + colName + ": ");
                if(colName.equals("d_name") || colName.equals("pt_name") || colName.equals("p_name") || colName.equals("t_genus"))
                {
                    info = dinoName;
                    System.out.print(info + "(already filled in)");
                    System.out.println();
                    pre.setString(i, info);
                    continue;
                }
                else if(colName.equals("d_dinokey") ||colName.equals("f_dinokey") || colName.equals("pt_dinokey") || colName.equals("t_dinokey"))
                {
                    info = dinoKey;
                    System.out.print(info + "(already filled in)");
                    System.out.println();
                    pre.setString(i, info);
                    continue;
                }
                info = input.nextLine();
                info = info.toLowerCase();
            
                pre.setString(i, info);
                System.out.println(uiDivider);
            }
            pre.executeUpdate();
            counter++;
        
        }
        System.out.println(uiDivider);
        System.out.print("Enter Nation: ");
        System.out.println(uiDivider);
        info = input.nextLine();
        info = info.toLowerCase();
        System.out.println();

        pre = conn.prepareStatement(findDinoLoc2);
        pre.setString(1, info);
        result = pre.executeQuery();
        if(result.next())
        {
            String loc = result.getString("l_dinokey");
            String locNation = result.getString("l_nation");
            loc = loc + dinoKey + ",";
            pre = conn.prepareStatement(updateDinoLoc);
            pre.setString(1, loc);
            pre.setString(2, locNation);
            pre.executeUpdate();
        }
        else
            System.out.println("nation not found.");
        
        
        System.out.println("New Data Inserted Successfully");
        pre.close();
        stmt.close();
        result.close();

    }

    public static void updateData(Connection conn, Scanner input) throws SQLException
    {
        String quickFix = input.nextLine();
        ResultSet result = null;
        Statement stmt = conn.createStatement();
        PreparedStatement pre = null;

        String info, preStmt, value1, value2 = "";
        String res = "";
        String tbl = "";
        String setAttrib = "";
        String setAttrib2 = "";
        int num, dinoKey = 0;
        
        System.out.println("What Table Would You like to update: 1 for Yes, 2 for No");
        for(int i = 0; i < tableNames.length; i++)
        {
            System.out.print(tableNames[i] + "?: ");
            try
            {
              num = input.nextInt();
            }
            catch(InputMismatchException e)
            {
              System.out.println("Invalid Input.");
              input.next();
              return;
            }
            if(num == 1)
            {
                tbl = tableNames[i];
                if(tbl.equals("location"))
                    break;
                res = tables[i];
                break;
            }
        }

        if(tbl.equals("location"))
        {
            quickFix = input.nextLine();

            System.out.print("Enter Dinosaur Name: ");
            info = input.nextLine();
            info = formatString(info);
            System.out.println();

            pre = conn.prepareStatement(dinoCheck);
            pre.setString(1, info);
            result = pre.executeQuery();
            if(result.next())
                dinoKey = result.getInt(2);
            else
            {
                System.out.println("Dinosaur not found.");
                return;
            }
            
            System.out.print("Enter Nation: ");
            info = input.nextLine();
            info = info.toLowerCase();
            System.out.println();

            pre = conn.prepareStatement(findDinoLoc2);
            pre.setString(1, info);
            result = pre.executeQuery();
            if(result.next())
            {
             String loc = result.getString("l_dinokey");
                String locNation = result.getString("l_nation");
                loc = loc + dinoKey + ",";
              
                pre = conn.prepareStatement(updateDinoLoc);
                pre.setString(1, loc);
                pre.setString(2, locNation);
             pre.executeUpdate();
            }
            else
              System.out.println("nation not found.");
            
            System.out.println("Update Executed."); 
            pre.close();
            result.close();
            stmt.close();
            return;
        
        }
        result = stmt.executeQuery(res);
        ResultSetMetaData resultMeta = result.getMetaData();
        int colNumber = resultMeta.getColumnCount();
        System.out.println("Attribute You'd Like to Set: 1 for Yes, 2 for No");
        for(int i = 1; i <= colNumber; i++)
        {
            System.out.print(resultMeta.getColumnName(i) + "?: ");
            try
            {
             num = input.nextInt();
            }
            catch(InputMismatchException e)
            {
              System.out.println("Invalid Input.");
              input.next();
              return;
            }
            if(num == 1)
            {
                setAttrib = resultMeta.getColumnName(i);
                break;
            }

        }

        System.out.println("Set Condition Attribute For Update To Execute: 1 for Yes, 2 for No");
        for(int i = 1; i <= colNumber; i++)
        {
            System.out.print(resultMeta.getColumnName(i) + "?: ");
            try
            {
              num = input.nextInt();
            }
            catch(InputMismatchException e)
            {
              System.out.println("Invalid Input.");
              input.next();
              return;
            }
            if(num == 1)
            {
                setAttrib2 = resultMeta.getColumnName(i);
                break;
            }

        }

        preStmt = "update " + tbl + " set " + setAttrib + " = ? where " + setAttrib2 + " = ?";
        //System.out.println(preStmt);
        pre = conn.prepareStatement(preStmt);

        quickFix = input.nextLine();

        System.out.print("Set value for " + setAttrib + ": ");
        value1 = input.nextLine();
        if(setAttrib.equals("d_name") || setAttrib.equals("pt_name") || setAttrib.equals("p_name") || setAttrib.equals("t_genus"))
            value1 = formatString(value1);
        else
            value1 = value1.toLowerCase();
        System.out.println();

        System.out.print("Set Condition Value For " + setAttrib2 + ": ");
        value2 = input.nextLine();
        if(setAttrib.equals("d_name") || setAttrib.equals("pt_name") || setAttrib.equals("p_name") || setAttrib.equals("t_genus"))
            value2 = formatString(value2);
        else
            value2 = value2.toLowerCase();
        System.out.println();

        pre.setString(1, value1);
        pre.setString(2, value2);

        System.out.println("Update Executed.");
        pre.executeUpdate(); 

        pre.close();
        result.close();
        stmt.close();
        //resultMeta.close();
    }

    public static void updateUser(Connection conn, Scanner input) throws SQLException
    {
        ResultSet result = null;
        PreparedStatement pre = conn.prepareStatement(updateStatus);
        Statement stmt = conn.createStatement();
        String quickFix = input.nextLine();
        String user = "";
        String oldStatus = "";
        String newStatus = "";
        int in = 0;

        System.out.print("Which User's Status Would You Like to Update?: 1 for Yes, 2 for No");
        result = stmt.executeQuery(selUser);
        System.out.println();

        while(result.next())
        {
            System.out.print("User: " + result.getString(1) + " Status: " + result.getString(2) + "?: ");
            try
            {
              in = input.nextInt();
            }
            catch(InputMismatchException e)
            {
              System.out.println("Invalid Input.");
              input.next();
              return;
            }
            if(in == 1)
            {
                user = result.getString(1);
                oldStatus = result.getString(2);
                break;
            }
        }
        System.out.println();
        quickFix = input.nextLine();
        System.out.println("Options: User, Hist, Admin");
        System.out.print("What New Status Would You Like?: ");
        newStatus = input.nextLine();
        newStatus = newStatus.toUpperCase();
        System.out.println();

        if(newStatus.equals("USER") || newStatus.equals("HIST") || newStatus.equals("ADMIN"))
        {
           pre.setString(1, newStatus);
           pre.setString(2, user);
           pre.executeUpdate();
           System.out.println("Update Executed.");
           System.out.println("User " + user + " Old Status: " + oldStatus);
           System.out.println("User " + user + " New Status: " + newStatus);

        }
        else
            System.out.println("Invalid Input.");
        
        pre.close();
        result.close();
        stmt.close();
    }

    public static void updateRequest(Connection conn, Scanner input) throws SQLException
    {
        PreparedStatement pre = conn.prepareStatement(updateReq);
        String quickFix = input.nextLine();
        int key = 0;

        System.out.print("Enter Request Key You'd like to update: ");
        try
            {
              key = input.nextInt();
            }
            catch(InputMismatchException e)
            {
              System.out.println("Invalid Input.");
              input.next();
              return;
            }
        System.out.println();
        pre.setInt(1, key);
        pre.executeUpdate();

        System.out.println("Update Executed.");
        pre.close();
    }
    public static void delRequest(Connection conn, Scanner input) throws SQLException
    {
        Statement stmt = conn.createStatement();
        String quickFix = input.nextLine();
        String uSure = "";

        System.out.println("Are you sure you want to delete requests from database?");
        System.out.print("Enter 'Yes' or 'No': ");
        uSure = input.nextLine();
        uSure = uSure.toLowerCase();
        System.out.println();

        if(uSure.equals("yes"))
        {
            stmt.executeUpdate(delReq);
            System.out.println("Requests Deleted.");
        }
        else if(uSure.equals("no"))
            System.out.println("Deletion Cancelled.");
        
        stmt.close();
    }
}