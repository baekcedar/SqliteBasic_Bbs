package com.baekcedar.android.sqlitebasic_bbs;

import java.util.ArrayList;

/**
 * Created by HM on 2016-10-11.
 */
public class Datas {

    public Datas(){
    }
    public static ArrayList<Data> newInstance(){
        return  new  ArrayList<Data>();
    }
}

class Data {
    int no;
    String name;
    String title;
    String contents;
    String ndate;
}
