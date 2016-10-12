package com.baekcedar.android.sqlitebasic_bbs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class ListFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;
    Button writeBtn, searchBtn;
    ListAdapter adapter;
    int listCount =10;
    int totalCount=0;
    EditText etSearch;
    private OnFragmentInteractionListener mListener;

    public ListFragment() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("TEST","onDestroyView");
    }

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =  inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        writeBtn = (Button) view.findViewById(R.id.writeBtn);

        etSearch = (EditText)view.findViewById(R.id.etSearch);
        searchBtn = (Button) view.findViewById(R.id.searchBtn);


        adapter = new ListAdapter(getActivity(),mListener.getDatas(10));
        listView.setAdapter(adapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String word = etSearch.getText().toString();
                if( !word.equals("")){
                    totalCount = Dbconnect.selectCount(getContext(), word);
                    adapter.listRefresh( Dbconnect.SelectAllbyWord(getContext(),10 , word) );

                }
            }
        });
       // write 버튼
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteraction(MainActivity.WRITE_PAGE);

            }
        });
        // List Item Click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                mListener.setBox(position);
                mListener.onFragmentInteraction(MainActivity.EDIT_PAGE);

               // 클릭시 페이지 이동
            }
        });


        // 스크롤 리스너
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if( totalItemCount == firstVisibleItem + visibleItemCount){
                    Log.i("TEST totalItemCount", ":" + totalItemCount);
                    Log.i("TEST totalCount", ":" + totalCount);
                    Log.i("TEST listCount", ":" + listCount);

                    if( totalItemCount < totalCount) {
                        listCount = listCount + 10;

                        adapter.listAdd(setListScroll(listCount));

                    }

                }
            }
        });
        return view;
    }


    public void setList(int listCount){
        totalCount= Dbconnect.selectCount(getContext(), "");
        mListener.setDatas( Dbconnect.listPrint(getContext(), listCount) );
    }
    public ArrayList<Data> setListScroll(int listCount){
        totalCount= Dbconnect.selectCount(getContext(), "");
        return  Dbconnect.listPrintScroll(getContext(), listCount);
    }
    public ArrayList<Data> searchListScroll(int listCount){
        totalCount= Dbconnect.selectCount(getContext(), "");
        return  Dbconnect.listPrintScroll(getContext(), listCount);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 부모 activity에 interface가 구현되지 않았으면 Exception을 발생시켜 강제로 App을 종료시킨다
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        setList(listCount);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
