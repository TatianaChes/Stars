package com.example.starview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.starview.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class PhotoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView txtPrediction;
    Button Predict, Choise, InfoStar;
    int requesCode;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Bundle arguments = getIntent().getExtras();
        requesCode = arguments.getInt("requestCode");

        imageView = findViewById(R.id.imageView);
        Predict = findViewById(R.id.Predict);
        Choise = findViewById(R.id.Choise);
        InfoStar = findViewById(R.id.InfoStar);
        txtPrediction = findViewById(R.id.textView2);

        Working();
    }

    private void Working() {
        if(requesCode == 1){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), 1);
        } else{
            Choise.setText("Камера");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 12);
        }

    }

    public void PredictPhoto(View view) {
        if(bitmap != null) {
            try {
                Model model = Model.newInstance(getApplicationContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                TensorImage tensorImage = new TensorImage(DataType.UINT8);
                tensorImage.load(bitmap);
                ByteBuffer byteBuffer = tensorImage.getBuffer();

                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                Model.Outputs outputs = model.process(inputFeature0);

                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                String[] labels = new String[10];
                int cnt = 0;
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        labels[cnt] = line;
                        cnt++;
                        line = bufferedReader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                float[] confidences = outputFeature0.getFloatArray();
                int maxPos = 0;
                float maxConfidence = 0;
                for (int i = 0; i < confidences.length; i++) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i];
                        maxPos = i;
                        //  Log.d("LOOK",labels[i] + " " + String.valueOf(confidences[i]));

                    }
                }
                model.close();
                txtPrediction.setText(labels[maxPos]);
                //  Log.d("LOOK", labels[maxPos]);
                //  Log.d("LOOK", labels[getMax(outputFeature0.getFloatArray())]+ "");
                //  Toast.makeText(MenuActivity.this,labels[getMax(outputFeature0.getFloatArray())]+ "", Toast.LENGTH_LONG);
                // Releases model resources if no longer used.

            } catch (IOException e) {
                // TODO Handle the exception
            }
        } else{
            Toast.makeText(PhotoActivity.this, "Пожалуйста установите фото!", Toast.LENGTH_LONG).show();
        }
    }

    public void ChoisePhoto(View view) {
        Working();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                try{

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                    //   Analis();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        if(requestCode == 12 && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
           // Analis();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public void InfoOpen(View view) {
        Intent intent = new Intent(PhotoActivity.this, PersonActivity.class);
        intent.putExtra("Name", txtPrediction.getText().toString());
        startActivity(intent);
    }
}