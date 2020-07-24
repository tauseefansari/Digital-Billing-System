package com.tauseef.mybills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mNavigationView;
    private TextView mAmount, mDate;
    private static final String MY_PREFS = "bill";
    private static final String MY_DATE = "date";
    private static final String MY_PRICE = "price";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

        mNavigationView = findViewById(R.id.bottom_navigation);
        mAmount = findViewById(R.id.amount);
        mDate = findViewById(R.id.date);
        mNavigationView.setSelectedItemId(R.id.home);

        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.view:
                        startActivity(new Intent(MainActivity.this, DisplayLayout.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.add:
                        startActivity(new Intent(MainActivity.this, AddLayout.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        String date = mPreferences.getString(MY_DATE, "No Updates");
        String amount = mPreferences.getString(MY_PRICE, "0");

        mDate.setText(date);
        mAmount.setText(amount+" Rs.");
    }
}