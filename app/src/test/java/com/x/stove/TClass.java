package com.x.stove;

public class TClass {

    static {
        System.out.println("静态代码块");
    }

    private String SS1 = getSS1();


    private String getSS1() {
        System.out.println("非静态变量111");
        return "SSSSS11111";
    }


    private static String SS = getSS();


    private static String getSS() {
        System.out.println("静态变量");
        return "SSSSS";
    }


    public static void SS() {
        System.out.println("静态方法-无返回值");

    }

    public TClass() {
        System.out.println("TClass");
    }

    public void print() {
        System.out.println("结束");
    }
}
