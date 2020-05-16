package com.example.lab03part02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyXMLActivity";
    private EditText editTextMake;
    private EditText editTextYear;
    private EditText editTextColor;
    private EditText editTextPrice;
    private EditText editTextEngine;
    private TextView textViewBlock;
    private Vehicle vehicle;
    private ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
    private StringBuilder outputs;
    private static Double depreciation;

    private Map<String, String> mapCarMaker = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        editTextMake = (EditText) findViewById(R.id.inputMake);
        editTextYear = (EditText) findViewById(R.id.inputYear);
        editTextColor = (EditText) findViewById(R.id.inputColor);
        editTextPrice = (EditText) findViewById(R.id.inputPrice);
        editTextEngine = (EditText) findViewById(R.id.inputEngine);
        textViewBlock = (TextView) findViewById(R.id.textBlock);
        textViewBlock.setMovementMethod(new ScrollingMovementMethod());
        depreciation = getResources().getInteger(R.integer.depreciation) / 100.00;

        String[] manufacturers = getResources().getStringArray(R.array.manufacturer_array);
        String[] descriptions = getResources().getStringArray(R.array.description_array);
        for (int i = 0; i < manufacturers.length; i++) {
            mapCarMaker.put(manufacturers[i], descriptions[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_add:
                addVehicle();
                return true;
            case R.id.menu_clear:
                return clearVehicleList();
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addVehicle() {
        if(vehicle!=null)
            vehicleArrayList.add(vehicle);
        resetOutputs();
    }

    private boolean clearVehicleList() {
        vehicleArrayList.clear();
        resetOutputs();
        return true;
    }

    private void resetOutputs() {
        if (vehicleArrayList.size() == 0) {
            outputs = new StringBuilder("Your vehicle list is currently empty.");
        } else {
            outputs = new StringBuilder();
            for(Vehicle vehicle: vehicleArrayList) {
                String vehicleDescription = mapCarMaker.get(vehicle.getMake());
                if(vehicleDescription==null) {
                    vehicleDescription = "No description available";
                }
                outputs.append("This is vehicle No. " + (vehicleArrayList.indexOf(vehicle) + 1));
                outputs.append("Manufacturer: " + vehicle.getMake());
//                outputs.append("; Current value: " + depreciatePrice(vehicle.getPrice()));
//                outputs.append("; Effective engine size: " + depreciateEngine(vehicle.getEngine()));
                outputs.append("; Current value: " + depreciate(vehicle.getPrice()));
                outputs.append("; Effective engine size: " + depreciate(vehicle.getEngine()));

                outputs.append(System.getProperty("line.separator"));
                outputs.append(System.getProperty("line.separator"));
            }
        }
        textViewBlock.setText(outputs);
    }

    private int depreciatePrice(int price) {
        return (int) (price * depreciation);
    }

    private double depreciateEngine(double engine) {
        return (double) Math.round(engine * depreciation * 100) / 100;
    }

    private <T extends Number> Double depreciate(T originalValue) {
        Double result = Math.round(originalValue.doubleValue() * depreciation * 100) / 100.00;
        return result;
    }

    public void onBtnGoClick(View view) {

        String make = editTextMake.getText().toString();
        String strYear = editTextYear.getText().toString();
        int intYear = Integer.parseInt(strYear);
        String color = editTextColor.getText().toString();

        String strPrice = editTextPrice.getText().toString();
        //int price = Integer.parseInt(strPrice);
        Integer price = new Integer(strPrice);
        String strEngine = editTextEngine.getText().toString();
        //double engine = Double.parseDouble(strEngine);
        Double engine = new Double(strEngine);

        vehicle = new Vehicle();
        switch(view.getId()) {
            case R.id.buttonRunPetrol:
                vehicle = new PetrolCar(make, intYear, color, price, engine);
                break;
            case R.id.btnRunDiesel:
                vehicle = new DieselCar(make, intYear, price, engine);
                break;
            default:
                break;
        }

        if(Vehicle.counter == 5) {
            vehicle = new Vehicle() {
                @Override
                public  String getMessage() {
                    return "You have pressed 5 times, stop it!";
                }
            };
        }

        Toast.makeText(getApplicationContext(), vehicle.getMessage(), Toast.LENGTH_SHORT).show();

        Log.d(TAG, "User clicked " + Vehicle.counter + " times.");
        Log.d(TAG, "User message is \"" + vehicle + "\".");
        //Log.d(TAG, "onBtnGoClick() called with: view = [" + view + "]"); // Ctrl + j Live template

    }

    public void onClearClick(View view) {
        clearVehicleList();
    }
}
