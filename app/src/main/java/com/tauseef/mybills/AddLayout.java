package com.tauseef.mybills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddLayout extends AppCompatActivity {

    private TextInputLayout mDate, mStyle, mPrice, mStyleNo, mDescription;
    private Button submit,cancel;
    private DatabaseHelper mHelper;
    private BottomNavigationView mNavigationView;
    private static final String MY_PREFS = "bill";
    private static final String MY_DATE = "date";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layout);

        mPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

        mNavigationView = findViewById(R.id.bottom_navigation);
        mNavigationView.setSelectedItemId(R.id.add);

        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.view:
                        startActivity(new Intent(AddLayout.this, DisplayLayout.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(AddLayout.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add:
                        return true;
                }
                return false;
            }
        });

        mHelper = new DatabaseHelper(this);

        mDate = findViewById(R.id.date);
        mPrice = findViewById(R.id.price);
        mStyle = findViewById(R.id.style);
        mStyleNo = findViewById(R.id.style_no);
        mDescription = findViewById(R.id.description);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);

        Calendar now = Calendar.getInstance();
        final int day = now.get(Calendar.DAY_OF_MONTH);
        final int year = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH);

        mDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLayout.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        mDate.getEditText().setText(day+"-"+month+"-"+year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        mDate.setErrorIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLayout.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        mDate.getEditText().setText(day+"-"+month+"-"+year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateDate() | validateStyle() | validateStyleNo() | validateDescription() | validatePrice() )
                    return;
                else
                {
                    boolean res = mHelper.insertData(mDate.getEditText().getText().toString(), mStyle.getEditText().getText().toString(), mStyleNo.getEditText().getText().toString(), mDescription.getEditText().getText().toString(), Long.valueOf(mPrice.getEditText().getText().toString()));
                    if (res)
                    {
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString(MY_DATE, mDate.getEditText().getText().toString());
                        editor.commit();
                        mDate.getEditText().setText("");
                        mStyle.getEditText().setText("");
                        mStyleNo.getEditText().setText("");
                        mPrice.getEditText().setText("");
                        mDescription.getEditText().setText("");
                        Toast.makeText(AddLayout.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(AddLayout.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(AddLayout.this, MainActivity.class));
            }
        });

    }

    private boolean validateDate() {
        if (mDate.getEditText().getText().toString().trim().equals("") || !(mDate.getEditText().getText().toString().trim().contains("-")))
        {
            mDate.setError("Invalid Date! Use date picker");
            return true;
        }
        else
        {
            mDate.setError(null);
            return false;
        }
    }

    private boolean validateStyle()
    {
        if (mStyle.getEditText().getText().toString().trim().equals(""))
        {
            mStyle.setError("Please enter style");
            return true;
        }
        else
        {
            mStyle.setError(null);
            return false;
        }
    }

    private boolean validateStyleNo()
    {
        if (mStyleNo.getEditText().getText().toString().trim().equals(""))
        {
            mStyleNo.setError("Please enter style number");
            return true;
        }
        else
        {
            mStyleNo.setError(null);
            return false;
        }
    }

    private boolean validateDescription()
    {
        if (mDescription.getEditText().getText().toString().trim().equals(""))
        {
            mDescription.setError("Please enter description");
            return true;
        }
        else
        {
            mDescription.setError(null);
            return false;
        }
    }

    private boolean validatePrice()
    {
        if (mPrice.getEditText().getText().toString().trim().equals("") || Long.valueOf(mPrice.getEditText().getText().toString()) <= 0 )
        {
            mPrice.setError("Invalid price");
            return true;
        }
        else
        {
            mPrice.setError(null);
            return false;
        }
    }
}