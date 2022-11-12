package com.example.apitask;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class editApp extends AppCompatActivity implements View.OnClickListener {

    EditText appName, appAgeLimit, appPrice;
    Button saveChangesBtn, prevPageBtn, deleteBtn, deletePhoto;
    ImageView image;
    int id;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_app);

        getSupportActionBar().hide();

        appName = findViewById(R.id.appNameET);
        appAgeLimit = findViewById(R.id.appAgeLimitET);
        appPrice = findViewById(R.id.appPriceET);

        saveChangesBtn = findViewById(R.id.saveChangesBtn);
        saveChangesBtn.setOnClickListener(this);

        prevPageBtn = findViewById(R.id.prevPageBtn);
        prevPageBtn.setOnClickListener(this);

        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);

        deletePhoto = findViewById(R.id.deletePicBtn);
        deletePhoto.setOnClickListener(this);

        image = findViewById(R.id.appImageIV);
        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/МорозовАВ/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<DataModal> call = retrofitAPI.getData(MainActivity.index);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                id = response.body().getApp_id();
                appName.setText(response.body().getAppName());
                appAgeLimit.setText(Integer.valueOf(response.body().getAppAgeLimit()).toString());
                appPrice.setText(Double.valueOf(response.body().getAppPrice()).toString());
                encodedImage = response.body().getAppImage();
                if(response.body().getAppImage() == null)
                {
                    image.setImageResource(R.drawable.empty);
                }
                else
                {
                    image.setImageBitmap(getImgBitmap(response.body().getAppImage()));
                }
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {

            }
        });
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    image.setImageBitmap(bitmap);
                    encodedImage = encodeImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    });

    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }

    private Bitmap getImgBitmap(String encodedImg) {
        if (encodedImg != null) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(editApp.this.getResources(),
                R.drawable.empty);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.saveChangesBtn:
                String aName = appName.getText().toString();
                double aPrice = Double.valueOf(appPrice.getText().toString());
                int aAgeLimit = Integer.valueOf(appAgeLimit.getText().toString());

                putChanges(id, aName, aPrice, aAgeLimit, encodedImage);
                break;
            case R.id.prevPageBtn:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.deleteBtn:
                deleteRecord(MainActivity.index);
                break;
            case R.id.deletePicBtn:
                image.setImageResource(R.drawable.empty);
                encodedImage = null;
                break;
        }
    }

    private void putChanges(int id, String appName, double appPrice, int appAgeLimit, String picture) {

        saveChangesBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        deletePhoto.setEnabled(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/ngknn/морозовав/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        DataModal modal = new DataModal(id, appName, appPrice, appAgeLimit, picture);
        Call<DataModal> call = retrofitAPI.updateData(MainActivity.index, modal);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                if(response.isSuccessful())
                {
                    saveChangesBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                    deletePhoto.setEnabled(true);

                    Toast.makeText(editApp.this, "Изменение прошло успешно", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(editApp.this, MainActivity.class));
                    finish();
                    return;
                }
                Toast.makeText(editApp.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
            }
        });
    }

    private void deleteRecord(int index)
    {
        saveChangesBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        deletePhoto.setEnabled(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/МорозовАВ/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call call = retrofitAPI.deleteData(index);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(editApp.this, "При удание записи возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveChangesBtn.setEnabled(false);
                deleteBtn.setEnabled(false);
                deletePhoto.setEnabled(false);

                Toast.makeText(editApp.this, "Удаление прошло успешно", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(editApp.this, MainActivity.class));
                finish();
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {

            }
        });
    }
}