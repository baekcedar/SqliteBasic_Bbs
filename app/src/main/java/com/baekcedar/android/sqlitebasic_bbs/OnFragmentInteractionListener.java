package com.baekcedar.android.sqlitebasic_bbs;

import java.util.ArrayList;


public interface OnFragmentInteractionListener {
    void onFragmentInteraction(int actionFlag);
    ArrayList<Data> getDatas();
    Data getData(int position);

    void setInsert(Data data);
    void setUpdate(Data data);
    void setDelete(int position);

    void setBox(int position);

    void listRefresh();

    int getUpdateFlag();


}
