package com.x.stove;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Houxingxiu
 * Date : 2018/12/3
 */
public class StoveLayoutInflaterFactory implements LayoutInflater.Factory2 {

    private AppCompatDelegate mAppCompatDelegate;

    public StoveLayoutInflaterFactory(AppCompatDelegate appCompatDelegate) {
        mAppCompatDelegate = appCompatDelegate;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        View view = mAppCompatDelegate.createView(parent, name, context, attrs);
        if (TextUtils.equals(name, "TextView")) {
            if (view instanceof AppCompatTextView) {
                AppCompatTextView textView = (AppCompatTextView) view;
                textView.setText("我是个TextView");
//                textView.animate().rotation(360 * 10).setDuration(10 * 1000).start();
            }
        }

        return view;
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
