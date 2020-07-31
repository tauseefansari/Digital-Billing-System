package com.tauseef.mybills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final static int ID_ADD = 1;
    private final static int ID_HOME = 2;
    private final static int ID_VIEW = 3;


    private TextView mAmount, mDate;
    private static final String MY_PREFS = "bill";
    private static final String MY_DATE = "date";
    private static final String MY_PRICE = "price";

    private DatabaseHelper mHelper;

    private PieChart mPieChart;
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DatabaseHelper(this);

        final MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ADD, R.drawable.ic_add));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_VIEW, R.drawable.ic_view));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId())
                {
                    case ID_ADD:
                        //Toast.makeText(MainActivity.this, "Add Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, AddLayout.class));
                        overridePendingTransition(0,0);
                        break;
                    case ID_VIEW:
                        //Toast.makeText(MainActivity.this, "View Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, DisplayLayout.class));
                        overridePendingTransition(0,0);
                        break;
                }
            }
        });

        bottomNavigation.show(ID_HOME, true);

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                if (item.getId() == ID_HOME)
                {
                    Toast.makeText(MainActivity.this, "Home Already Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPieChart = findViewById(R.id.pieChart);
        Legend legend = mPieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(16f);
        ArrayList<PieEntry> pieEntries = mHelper.chartData();
        //Log.d(TAG, "onCreate: First Entry is : "+pieEntries.get(0));
        //Toast.makeText(MainActivity.this, "First Entry is : "+pieEntries.get(0), Toast.LENGTH_SHORT).show();
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);
        mPieChart.setData(pieData);
        mPieChart.setCenterText("Billing \nInformation");
        mPieChart.setCenterTextSize(15f);
        mPieChart.getDescription().setText("Billing Information");
        mPieChart.getDescription().setTextSize(15f);
        mPieChart.getDescription().setTextColor(Color.WHITE);
        mPieChart.animateY(2000);
        mPieChart.invalidate();

        mPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);


        mAmount = findViewById(R.id.amount);
        mDate = findViewById(R.id.date);
        /*mNavigationView.setSelectedItemId(R.id.home);

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
        });*/

        String date = mPreferences.getString(MY_DATE, "No Updates");
        String amount = mPreferences.getString(MY_PRICE, "0");

        mDate.setText(date);
        mAmount.setText(amount+" Rs.");
    }
}