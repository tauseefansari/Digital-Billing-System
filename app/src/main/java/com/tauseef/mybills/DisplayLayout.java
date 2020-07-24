package com.tauseef.mybills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DisplayLayout extends AppCompatActivity {

    private BottomNavigationView mNavigationView;
    private TextView mTitle;
    private TableLayout mTable;
    private DatabaseHelper mHelper;
    private static final String MY_PREFS = "bill";
    private static final String MY_PRICE = "price";
    public static long totals;
    SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_layout);

        mHelper = new DatabaseHelper(this);

        mTitle = findViewById(R.id.title);
        mNavigationView = findViewById(R.id.bottom_navigation);
        mNavigationView.setSelectedItemId(R.id.view);
        mTable = findViewById(R.id.view_table);

        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.view:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(DisplayLayout.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add:
                        startActivity(new Intent(DisplayLayout.this, AddLayout.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });



        Cursor records = mHelper.getAllItenms();
        if (records.getCount() == 0)
        {
            mTable.setVisibility(View.GONE);
            mTitle.setText("No records to show");
            return;
        }
        else
            while (records.moveToNext())
            {
                totals = totals + Long.valueOf(records.getString(5));

                TableRow row = new TableRow(this);
                TextView date = new TextView(this);
                date.setText(records.getString(1));
                date.setBackgroundColor(Color.WHITE);
                date.setTextColor(Color.BLACK);
                date.setTextSize(18);
                date.setPadding(5, 0, 10, 0);
                date.setGravity(Gravity.CENTER);
                row.addView(date);

                TextView styleno = new TextView(this);
                styleno.setText(records.getString(3));
                styleno.setBackgroundColor(Color.WHITE);
                styleno.setTextColor(Color.BLACK);
                styleno.setTextSize(18);
                styleno.setPadding(5, 0, 10, 0);
                styleno.setGravity(Gravity.CENTER);
                row.addView(styleno);

                TextView style = new TextView(this);
                style.setText(records.getString(2));
                style.setBackgroundColor(Color.WHITE);
                style.setTextColor(Color.BLACK);
                style.setTextSize(18);
                style.setPadding(5, 0, 10, 0);
                style.setGravity(Gravity.CENTER);
                row.addView(style);

                TextView desc = new TextView(this);
                desc.setText(records.getString(4));
                desc.setBackgroundColor(Color.WHITE);
                desc.setTextColor(Color.BLACK);
                desc.setTextSize(18);
                desc.setPadding(5, 0, 10, 0);
                desc.setGravity(Gravity.CENTER);
                row.addView(desc);

                TextView price = new TextView(this);
                price.setText(records.getString(5));
                price.setBackgroundColor(Color.WHITE);
                price.setTextColor(Color.BLACK);
                price.setTextSize(18);
                price.setPadding(5, 0, 10, 0);
                price.setGravity(Gravity.CENTER);
                row.addView(price);


                mTable.addView(row);
            }

        TableRow last = new TableRow(this);
        TextView total = new TextView(this);
        last.addView(total);

        TableRow.LayoutParams layoutParams = (TableRow.LayoutParams) total.getLayoutParams();
        layoutParams.span = 4;
        total.setLayoutParams(layoutParams);
        total.setText("Total");
        total.setTextColor(Color.WHITE);
        total.setTypeface(null, Typeface.BOLD);
        total.setGravity(Gravity.CENTER);
        total.setPadding(10,10,10,10);
        total.setTextSize(20);
        total.setBackgroundColor(Color.RED);

        TextView total_value = new TextView(this);
        total_value.setText(Long.toString(totals));
        total_value.setTextColor(Color.WHITE);
        total_value.setTypeface(null, Typeface.BOLD);
        total_value.setGravity(Gravity.CENTER);
        total_value.setPadding(10,10,10,10);
        total_value.setTextSize(20);
        total_value.setBackgroundColor(Color.RED);

        last.addView(total_value);

        mTable.addView(last);

        mPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(MY_PRICE, Long.toString(totals));
        editor.commit();
    }

}