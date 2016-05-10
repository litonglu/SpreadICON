package com.wangdao.our.spread_2.fragment_dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangdao.our.spread_2.R;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
public class FragmentDialog_2 extends Fragment{

    private Context myContext;
    private View myView;
    private LayoutInflater myInflater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_dialog_2,null);
        myContext = this.getActivity();
        myInflater = inflater;
        return myView;

    }
}
