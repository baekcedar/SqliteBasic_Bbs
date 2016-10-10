package com.baekcedar.android.sqlitebasic_bbs;

import java.util.ArrayList;


public interface OnFragmentInteractionListener {
    void onFragmentInteraction(int pageFlag);
    ArrayList<Data> getDatas();
    Data getData(int position);
    void setUpdate(Data data);
    void setInsert(Data data);
    void setPosition(int position);
    void setBox(int position);

    int getUpdateFlag();


}
