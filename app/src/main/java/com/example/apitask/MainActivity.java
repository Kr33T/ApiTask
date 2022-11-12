package com.example.apitask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AdapterApps adapterApps;
    private List<apps> appsList = new ArrayList<>();

    Spinner orderByItem, descOrAsc;

    Boolean orderBy;
    Button addNew, clearSort;
    ListView list;
    EditText searchET;
    public static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        orderBy = true;

        addNew = findViewById(R.id.addNewRecord);
        addNew.setOnClickListener(this);

        ListView lvApps = findViewById(R.id.appsList);
        adapterApps = new AdapterApps(MainActivity.this, appsList);
        lvApps.setAdapter(adapterApps);

        searchET = findViewById(R.id.searchET);
        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                new GetApps().execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        clearSort = findViewById(R.id.clearOrderBy);
        clearSort.setOnClickListener(this);

        orderByItem = findViewById(R.id.orderByItem);
        orderByItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new GetApps().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        descOrAsc = findViewById(R.id.descOrAsc);
        descOrAsc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(descOrAsc.getSelectedItemPosition())
                {
                    case 0:
                        orderBy = true;
                        break;
                    case 1:
                        orderBy = false;
                        break;
                }
                new GetApps().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        list = findViewById(R.id.appsList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = (int)l;

                transition();
            }
        });

        new GetApps().execute();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.addNewRecord:
                startActivity(new Intent(this, addNewRecord.class));
                finish();
                break;
            case R.id.clearOrderBy:
                orderByItem.setSelection(0);
                descOrAsc.setSelection(0);
                searchET.setText("");
                new GetApps().execute();
                break;
        }
    }

    public void transition()
    {
        Intent intent = new Intent(this, editApp.class);
        startActivity(intent);
        finish();
    }

    private class GetApps extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids)
        {
            try
            {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/МорозовАВ/api/apps/search?searchAppName=" + searchET.getText().toString() + "&order=" + orderBy.toString() + "&field=" + orderByItem.getSelectedItemPosition());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                appsList.clear();
                JSONArray tempArray = new JSONArray(s);
                for(int i = 0; i < tempArray.length(); i++)
                {
                    JSONObject appJson = tempArray.getJSONObject(i);
                    apps tempApp = new apps(
                            appJson.getInt("app_id"),
                            appJson.getString("appName"),
                            appJson.getDouble("appPrice"),
                            appJson.getInt("appAgeLimit"),
                            appJson.getString("appImage")
                    );
                    appsList.add(tempApp);
                    adapterApps.notifyDataSetInvalidated();
                }
            }
            catch (Exception ignored)
            {

            }
        }
    }
}