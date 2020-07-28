package com.tauseef.mybills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class DisplayLayout extends AppCompatActivity {

    private final static int ID_ADD = 1;
    private final static int ID_HOME = 2;
    private final static int ID_VIEW = 3;

    private TextView mTitle;
    private TableLayout mTable;
    private DatabaseHelper mHelper;
    private Button pdf;
    private static final String MY_PREFS = "bill";
    private static final String MY_PRICE = "price";
    private static final String MY_DATE = "date";
    private static final int REQUEST_CODE = 111;
    private File pdfFile;
    public static long totals;
    SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_layout);

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
                        //Toast.makeText(ViewLayout.this, "Add Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DisplayLayout.this, AddLayout.class));
                        overridePendingTransition(0,0);
                        break;
                    case ID_HOME:
                        //Toast.makeText(ViewLayout.this, "Home Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DisplayLayout.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case ID_VIEW:
                        Toast.makeText(DisplayLayout.this, "Display Already Selected", Toast.LENGTH_SHORT).show();
                        return;
                        //Intent intent = getIntent();
                        //finish();
                        //startActivity(intent);
                }
            }
        });

        bottomNavigation.show(ID_VIEW, true);

        mHelper = new DatabaseHelper(this);

        mTitle = findViewById(R.id.title);

        mTable = findViewById(R.id.view_table);
        pdf = findViewById(R.id.pdf);


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(DisplayLayout.this, "Creating PDF...", Toast.LENGTH_SHORT).show();
                //pdfWritePermission();
                int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(DisplayLayout.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if(!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS))
                        {
                            showMessage("You need to allow access to storage", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                                }
                            });
                            return;
                        }
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    }
                }
                else
                {
                    ProgressThread progress = new ProgressThread();
                    progress.execute();
                }
            }

            private void showMessage(String message, DialogInterface.OnClickListener okListener) {
                new MaterialAlertDialogBuilder(DisplayLayout.this)
                        .setMessage(message)
                        .setPositiveButton("OK", okListener)
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        });




        Cursor records = mHelper.getAllItems();
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
                date.setTextSize(20);
                date.setPadding(5, 0, 10, 30);
                date.setGravity(Gravity.CENTER);
                row.addView(date);

                TextView styleno = new TextView(this);
                styleno.setText(records.getString(3));
                styleno.setBackgroundColor(Color.WHITE);
                styleno.setTextColor(Color.BLACK);
                styleno.setTextSize(20);
                styleno.setPadding(5, 0, 10, 30);
                styleno.setGravity(Gravity.CENTER);
                row.addView(styleno);

                TextView style = new TextView(this);
                style.setText(records.getString(2));
                style.setBackgroundColor(Color.WHITE);
                style.setTextColor(Color.BLACK);
                style.setTextSize(20);
                style.setPadding(5, 0, 10, 30);
                style.setGravity(Gravity.CENTER);
                row.addView(style);

                TextView desc = new TextView(this);
                desc.setText(records.getString(4));
                desc.setBackgroundColor(Color.WHITE);
                desc.setTextColor(Color.BLACK);
                desc.setTextSize(20);
                desc.setPadding(5, 0, 10, 30);
                desc.setGravity(Gravity.CENTER);
                row.addView(desc);

                TextView price = new TextView(this);
                price.setText(records.getString(5));
                price.setBackgroundColor(Color.WHITE);
                price.setTextColor(Color.BLACK);
                price.setTextSize(20);
                price.setPadding(5, 0, 10, 30);
                price.setGravity(Gravity.CENTER);
                row.addView(price);

                ImageButton edit = new ImageButton(this);
                final TextView id = new TextView(this);
                id.setVisibility(View.GONE);
                id.setText(records.getString(0));
                edit.setImageResource(R.drawable.ic_edit);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DisplayLayout.this, AddLayout.class);
                        intent.putExtra("id", id.getText().toString());
                        //Toast.makeText(DisplayLayout.this, "Id is : "+id.getText().toString(),Toast.LENGTH_SHORT).show();
                        //Log.d("checkid", records.getString(0));
                        startActivity(intent);
                    }
                });
                row.addView(edit);

                ImageButton delete = new ImageButton(DisplayLayout.this);
                delete.setImageResource(R.drawable.ic_delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DisplayLayout.this);
                        builder.setCancelable(true);
                        builder.setMessage("Do you confirm wan't to delete?");
                        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int res = mHelper.deleteData(id.getText().toString());
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                                if (res != 0)
                                    Toast.makeText(DisplayLayout.this, "Item Deleted ", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(DisplayLayout.this, "Item not Deleted ", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });

                row.addView(delete);


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

        updateSharedPrefs();

    }

    private void updateSharedPrefs() {
        mPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(MY_PRICE, Long.toString(totals));
        editor.commit();
    }

    private class ProgressThread extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog mDialog;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mDialog.isShowing())
            {
                mDialog.dismiss();
            }
            previewPDF();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(DisplayLayout.this);
            mDialog.setTitle("Creating PDF");
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage("Please Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                createPDF();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void previewPDF() {
            PackageManager packageManager = DisplayLayout.this.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("application/pdf");
            List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0)
            {
                Intent inte = new Intent();
                inte.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(pdfFile);
                inte.setDataAndType(uri, "application/pdf");
                inte.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(inte);
            }
            else
                Toast.makeText(DisplayLayout.this, "You dont have proper application to view this file", Toast.LENGTH_SHORT).show();
        }


        private void createPDF() throws FileNotFoundException, DocumentException {
            File docFolder = new File(Environment.getExternalStorageDirectory() + "/Bills");
            if (!docFolder.exists())
            {
                docFolder.mkdir();
            }

            String date = mPreferences.getString(MY_DATE, "Today");

            String pdfName = "Bill upto "+date+".pdf";
            pdfFile = new File(docFolder.getAbsolutePath(), pdfName);
            OutputStream outputStream = new FileOutputStream(pdfFile);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(new float[] {3, 3, 3, 5, 3});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(50);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            Font h = new Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.BOLDITALIC, BaseColor.RED);
            table.addCell(new Paragraph("Date",h));
            table.addCell(new Paragraph("Style No",h));
            table.addCell(new Paragraph("Style",h));
            table.addCell(new Paragraph("Description",h));
            table.addCell(new Paragraph("Price",h));
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int i=0; i < cells.length; i++ )
            {
                cells[i].setBackgroundColor(BaseColor.GRAY);
            }
            Cursor res = mHelper.getAllItems();
            while (res.moveToNext())
            {
                table.addCell(res.getString(1));
                table.addCell(res.getString(3));
                table.addCell(res.getString(2));
                table.addCell(res.getString(4));
                table.addCell(res.getString(5));
            }
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 40.0f, Font.BOLDITALIC, BaseColor.BLACK);
            Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.ITALIC, BaseColor.BLUE);
            PdfPCell total = new PdfPCell(new Phrase("Total",g));
            total.setColspan(4);
            total.setVerticalAlignment(Element.ALIGN_MIDDLE);
            total.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(total);
            table.addCell(new Phrase(Long.toString(totals),g));
            Paragraph heading = new Paragraph("Bill \n\n",f);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);
            document.add(table);
            document.close();
        }

        /*private void pdfWritePermission() {
            int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(DisplayLayout.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS))
                    {
                        showMessage("You need to allow access to storage", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                            }
                        });
                        return;
                    }
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }
            else
            {
                try {
                    createPDF();
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

}