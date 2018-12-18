/*
 * Copyright (c) 2018 OPTiM CORPORATION. (http://www.optim.co.jp/)
 * Permission to use, copy, modify and redistribute are strongly controlled
 * under the rights of OPTiM CORPORATION.
 */

package com.example.pcd_b_1807_006.myapplication;

import android.content.Context;

import com.example.pcd_b_1807_006.myapplication.sub.Child;

/**
 * optimal_biz_android_agent
 * Class: MainEnumString
 * Created by PCD-B-1807-006 on 2018/10/16.
 * <p>
 * Description:
 */
public class MainEnumString {
    public enum EnumString {

        LOCATION_ON(getRstring(R.string.location_measure_on)),
        LOCATION_OFF(getRstring(R.string.location_measure_off)),
        SDK_VERSION(getRstring(R.string.sdk_version));

        public EnumString getLocation( boolean b) {

            MySam.testA();
            Child.myChild();
            return b ? LOCATION_ON : LOCATION_OFF;
        }


        private String rString ;
        private EnumString() {
        }
        EnumString(String rstring) {
            this.rString = rstring;
        }
        public String rText(){
            return rString;
        }
    }


    private static Context cnt;
    private static String getRstring(int resourceId, Object... formatArgs){
        if (formatArgs != null && formatArgs.length > 0) {
            return cnt.getString(resourceId, formatArgs);
        }
       return cnt.getString(resourceId);
    }

    public static void setContext(Context argcnt) {
        cnt = argcnt;
    }
}
