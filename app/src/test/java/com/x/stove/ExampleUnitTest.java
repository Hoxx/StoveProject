package com.x.stove;

import org.junit.Test;

import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void name() {
        TClass tClass = new TClass();
        tClass.print();
        TClass.SS();
    }
}