package com.x.stove;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Houxingxiu
 * Date : 2018/12/13
 */
public class StoveLayoutInflater extends LayoutInflater {


    public static StoveLayoutInflater from1(Context context) {
        return new StoveLayoutInflater(context);
    }


    private StoveLayoutInflater(Context context) {
        super(context);
    }

    @Override
    public StoveLayoutInflater cloneInContext(Context newContext) {
        return new StoveLayoutInflater(newContext);
    }

    @Override
    public View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
        View view = from(getContext()).inflate(resource, root, root != null);

        if (view.getLayoutParams() != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

            Log.e("HXX-TAG---", "view:" + view.toString());
            Log.e("HXX-TAG---", "layoutParams.width:" + layoutParams.width);
            Log.e("HXX-TAG---", "layoutParams.height:" + layoutParams.height);
        }

        if (view instanceof TextView) {
            ((TextView) view).setText("哈哈");
        }
        return view;
    }
}
