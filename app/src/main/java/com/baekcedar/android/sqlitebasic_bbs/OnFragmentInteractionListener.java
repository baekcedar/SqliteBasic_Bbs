package com.baekcedar.android.sqlitebasic_bbs;

import java.util.ArrayList;


public interface OnFragmentInteractionListener {
    void onFragmentInteraction(int actionFlag);
    ArrayList<Data> getDatas(int listCount);
    Data getData(int position);

    void setInsert(Data data);
    void setUpdate(Data data);
    void setDelete(int position);
    void setDatas(ArrayList<Data> datas);
    void setBox(int position);

    void listRefresh(int bsNo);

    int getUpdateFlag();


}
