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

public class addNewRecord extends AppCompatActivity implements View.OnClickListener {

    Button addBtn, prevBtn;
    EditText appNameET, appAgeLimitET, appPriceET;
    ImageView image;

    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_record);

        getSupportActionBar().hide();

        appNameET = findViewById(R.id.appNameET1);
        appAgeLimitET = findViewById(R.id.appAgeLimitET1);
        appPriceET = findViewById(R.id.appPriceET1);

        addBtn = findViewById(R.id.addNewRecordBtn1);
        addBtn.setOnClickListener(this);

        prevBtn = findViewById(R.id.prevPageBtn1);
        prevBtn.setOnClickListener(this);

        image = findViewById(R.id.appImage);
        image.setImageResource(R.drawable.empty);

        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);        });
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

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.addNewRecordBtn1:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://ngknn.ru:5001/NGKNN/МорозовАВ/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
                DataModal modal = new DataModal(appNameET.getText().toString(), Double.parseDouble(appPriceET.getText().toString()), Integer.parseInt(appAgeLimitET.getText().toString()), encodedImage);
                Call<DataModal> call = retrofitAPI.createPost(modal);
                call.enqueue(new Callback<DataModal>() {
                    @Override
                    public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(addNewRecord.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(addNewRecord.this, "Данные добавлены", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<DataModal> call, Throwable t) {
                        Toast.makeText(addNewRecord.this, "Произошла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.prevPageBtn1:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}