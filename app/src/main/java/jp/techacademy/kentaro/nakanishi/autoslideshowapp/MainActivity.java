package jp.techacademy.kentaro.nakanishi.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    Button susumu;
    Button modoru;
    Button saisei;
    public int n=1;
    public int oldn=1;
    public int max=0; //画像の総数
    public boolean on = false; //自動再生ボタンのON/OFF
    public Cursor cursor;
    Handler mHandler;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//パーミッション確認

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }
    }
//パーミッション要請
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    Timer mTimer;   //タイマー
    int mTimerS;    //タイマー用の変数
    Handler mHandler = new Handler();   //なにこれ？

    private void getContentsInfo() {

        // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
        cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToFirst()) {
            do {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                Log.d("ANDROID", "URI : " + imageUri.toString());
                max += 1;
                Log.d("ANDROID", "画像番号 " + Integer.toString(max));
            } while (cursor.moveToNext());
        }

        cursor.moveToFirst();
        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        Long id = cursor.getLong(fieldIndex);
        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        Log.d("ANDROID", "URI : " + imageUri.toString());
        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
        imageVIew.setImageURI(imageUri);


        susumu = (Button) findViewById(R.id.susumu);
        modoru = (Button) findViewById(R.id.modoru);
        saisei = (Button) findViewById(R.id.saisei);

        saisei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (on == true) {
                    on = false;
                    modoru.setEnabled(true);
                    susumu.setEnabled(true);

                } else {
                    on = true;
                    modoru.setEnabled(false);
                    susumu.setEnabled(false);
                }
                mHandler = new Handler();
                Timer timer= new Timer();
                timer.schedule(new TimerTask() {
                mHandler.post(new Runnable(){
                        @Override
                        public void run (){
                            if (n == max) {
                                n = 1;
                            } else {
                                n += 1;
                            }
                            Log.d("ANDROID", "番号は" + Integer.toString(n));

                            show();
                        }
                    });
                },2000,2000);
        });

            susumu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (n == max) {
                        n = 1;
                    } else {
                        n += 1;
                    }
                    Log.d("ANDROID", "番号は" + Integer.toString(n));

                    show();
                }
            });
            modoru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (n == 1) {
                        n = max;
                    } else {
                        n -= 1;
                    }
                    Log.d("ANDROID", "番号は" + Integer.toString(n));

                    show();
                }
            });


        if (on == true) {

        }
    }
    public void show(){

        if (n == 1) {
            cursor.moveToFirst();
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("ANDROID", "URI : " + imageUri.toString());
            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);
            oldn = n;

        } else if (n == max) {
            cursor.moveToLast();
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("ANDROID", "URI : " + imageUri.toString());
            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);
            oldn = n;

        } else if (n >= oldn) {
            cursor.moveToNext();
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("ANDROID", "URI : " + imageUri.toString());
            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);
            oldn = n;

        } else if (n <= oldn) {
            cursor.moveToPrevious();
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            Log.d("ANDROID", "URI : " + imageUri.toString());
            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);
            oldn = n;
        }
    }
}