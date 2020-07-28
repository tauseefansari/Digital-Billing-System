package com.tauseef.mybills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddLayout extends AppCompatActivity {

    private final static int ID_ADD = 1;
    private final static int ID_HOME = 2;
    private final static int ID_VIEW = 3;

    private TextInputLayout mDate, mStyle, mPrice, mStyleNo, mDescription;
    private Button submit,cancel, update;
    private DatabaseHelper mHelper;
    private TextView mTitle;

    private static final String MY_PREFS = "bill";
    private static final String MY_DATE = "date";
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layout);

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom);
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
                        Toast.makeText(AddLayout.this, "Add Already Selected", Toast.LENGTH_SHORT).show();
                        //return;
                        //Intent intent = getIntent();
                        //finish();
                        //startActivity(intent);
                    case ID_HOME:
                        //Toast.makeText(AddLayout.this, "Home Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddLayout.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case ID_VIEW:
                        //Toast.makeText(AddLayout.this, "View Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddLayout.this, DisplayLayout.class));
                        overridePendingTransition(0,0);
                        break;
                }
            }
        });

        bottomNavigation.show(ID_ADD, true);

        mPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);


        mHelper = new DatabaseHelper(this);
        mTitle = findViewById(R.id.title);

        mDate = findViewById(R.id.date);
        mPrice = findViewById(R.id.price);
        mStyle = findViewById(R.id.style);
        mStyleNo = findViewById(R.id.style_no);
        mDescription = findViewById(R.id.description);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
        update = findViewById(R.id.update);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mTitle.setText("Edit Item");
            final String id = bundle.getString("id");
            Cursor row = mHelper.getOneRow(id);
            mDate.getEditText().setText(row.getString(1));
            mStyle.getEditText().setText(row.getString(2));
            mStyleNo.getEditText().setText(row.getString(3));
            mDescription.getEditText().setText(row.getString(4));
            mPrice.getEditText().setText(""+row.getString(5));
            submit.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean res = mHelper.updateRow(id, mDate.getEditText().getText().toString(), mStyle.getEditText().getText().toString(), mStyleNo.getEditText().getText().toString(), mDescription.getEditText().getText().toString(), Long.valueOf(mPrice.getEditText().getText().toString()));
                    if (res)
                    {
                        mDate.getEditText().setText("");
                        mStyle.getEditText().setText("");
                        mStyleNo.getEditText().setText("");
                        mPrice.getEditText().setText("");
                        mDescription.getEditText().setText("");
                        Toast.makeText(AddLayout.this, "Item updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddLayout.this, DisplayLayout.class));
                    }
                    else
                    {
                        Toast.makeText(AddLayout.this, "Item not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

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
                        startActivity(new Intent(AddLayout.this, DisplayLayout.class));
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