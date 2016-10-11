package com.baekcedar.android.sqlitebasic_bbs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EditFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    TextView textNo;
    EditText name, editTextTitle, editTextContents;
    Button cancelBtn,saveBtn;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit, container, false);
        Log.i("TEST","onCreateView");
        textNo          = (TextView) view.findViewById(R.id.textNo);
        name            = (EditText) view.findViewById(R.id.textName);
        editTextTitle   = (EditText) view.findViewById(R.id.editTextTitle);
        editTextContents= (EditText) view.findViewById(R.id.editTextContents);

        cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);

        // cancel 버튼 클릭시
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardOff();
                mListener.listRefresh();
                mListener.onFragmentInteraction(MainActivity.LIST_PAGE);

            }
        });
        // Save 버튼 클릭시
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data = new Data();
                data.contents = editTextContents.getText().toString();
                data.title = editTextTitle.getText().toString();
                data.name = name.getText().toString();



                //필드 공백 체크
                if(data.contents.equals("")){
                    Toast.makeText(getActivity(),"contents 를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if(data.title.equals("")){
                    Toast.makeText(getActivity(),"title 을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if(data.name.equals("")){
                    Toast.makeText(getActivity(),"name 을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(mListener.getUpdateFlag()==1) { // 업데이트
                        data.no = Integer.parseInt(textNo.getText().toString());
                        mListener.setUpdate(data);
                    }else if (mListener.getUpdateFlag()==0){ // 일반 쓰기
                        mListener.setInsert(data);
                    }
                    keyBoardOff();
                    mListener.listRefresh();
                    mListener.onFragmentInteraction(MainActivity.LIST_PAGE);
                }




            }
        });


        return view ;
    }
    public void setUpdateMode(Data data){
        editTextTitle.setText(data.title);
        editTextContents.setText(data.contents);
        name.setText(data.name);
        textNo.setText(Integer.toString(data.no));
    }
    public void setWirteMode(){
        editTextTitle.setText("");
        editTextContents.setText("");
        name.setText("");
        textNo.setText("");

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    protected void keyBoardOff() {  // 키보드 내리기
        View view = getActivity().getCurrentFocus();
        InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}
