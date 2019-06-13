package com.czq.practice;


public class App 
{
    public static void main( String[] args )
    {

        int num = 0;
        for (int i = 0; i < 100; i++) {
            num = num++;
//            num = ++num;
        }
        System.out.println(num);



    }
}
