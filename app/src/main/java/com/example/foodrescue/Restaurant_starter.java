package com.example.foodrescue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class Restaurant_starter extends AppCompatActivity {

    SQLiteDatabase FeedTheBelly;
    String emailNew;
    private static final int OPEN_REQUEST_CODE = 41;
    TextView totalItems, txtView;
    Button Confirm_Form;
    Button uploadFile;
    Spinner cuisineType;
    Spinner catagoryType;
    EditText expiryDate;
    EditText noOfPlates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_starter);
        Confirm_Form = findViewById(R.id.btnViewConfirm);
        uploadFile = findViewById(R.id.btnViewItemsAdded);
        totalItems = findViewById(R.id.txtViewShowItemAdded);
        cuisineType = findViewById(R.id.cuisine_types);
        catagoryType = findViewById(R.id.category_types);
        expiryDate = findViewById(R.id.editTextDate);
        noOfPlates = findViewById(R.id.editNumberOfPlates);
        //createDB();

        //Intent i = getIntent();

        // loadUserInformation();
//        Detail details = (Detail) i.getSerializableExtra("Details");
//        User user = (User)i.getSerializableExtra("User");
//        emailNew =  user.getEmail();
        //   txtView = findViewById(R.id.txtViewProfile);
       /* Intent i = getIntent();
        Detail details = (Detail) i.getSerializableExtra("Details");
        User user = (User)i.getSerializableExtra("User");
        String email =  user.getEmail();
        totalItems.setText(email);*/
        ActionBar myActionBar=getSupportActionBar();
        myActionBar.setTitle("Fill in the Food Details");

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, OPEN_REQUEST_CODE);
            }
        });

        Confirm_Form.setOnClickListener((View view)->{
            startActivity(new Intent(getApplicationContext(),recyclerViewActivity_Restaurant.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

                if (data != null && data.getData() != null) {

                    try {
                        List<Dishes> dishes = readFileContent(data.getData());
                        totalItems.setText("Total Items added:" + dishes.size());
                    } catch (IOException e) {
                        Log.d("IOException", "Unable to find File");
                    }
                }
            }
    }

    private List<Dishes> readFileContent(Uri uri) throws IOException {
        //        Intent myIntent = getIntent();
//        User user = (User)myIntent.getSerializableExtra("User");
//        email =  user.getEmail();
        List<Dishes> newDish = new ArrayList<>();

        InputStream inputStream = getContentResolver().openInputStream(uri);
        //        XSSFWorkbook myWorkBook = new XSSFWorkbook (inputStream);
//
//        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
//        Iterator<Row> rowIterator = mySheet.iterator();
//
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//            Iterator<Cell> cellIterator = row.cellIterator();
//            List<String> myList = new ArrayList<>();
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//                myList.add(cell.getStringCellValue());
//            }
//            Dishes dish = new Dishes(myList.get(0).toString(),Integer.parseInt(myList.get(1).toString()),Double.parseDouble(myList.get(2).toString()));
//            newDish.add(dish);
//        }

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        inputStream));
        String currentLine;
        currentLine = reader.readLine();
        int i = 1;
        while ((currentLine = reader.readLine()) != null) {
            String[] eachLine = currentLine.split(",");
            String nameOfDish = eachLine[0].toString();
            int noOfItems = Integer.parseInt(eachLine[1]);
            double weight = Double.parseDouble(eachLine[2]);
            Dishes dish = new Dishes(nameOfDish, noOfItems, weight);
            newDish.add(dish);
            String res = new DBmanager(this).addNewDish(dish, i, cuisineType,catagoryType, expiryDate);
            i++;
        }
        i = 0;
        inputStream.close();
        return newDish;
    }

}