package com.example.pcd_b_1807_006.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = (TextView) findViewById(R.id.test);

        MainEnumString.setContext(getApplicationContext());
        t.setText(MainEnumString.EnumString.SDK_VERSION.rText());
        Log.e("rT",MainEnumString.EnumString.LOCATION_ON.getLocation(false).rText());


        MySam.testA();

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();
        for(Account account: accounts) {
            Log.e("Account", account.name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
