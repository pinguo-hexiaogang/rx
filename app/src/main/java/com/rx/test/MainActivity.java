package com.rx.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Observers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        setImage();
    }

    private void setImage() {
        Observable.just("http://img2.zol.com.cn/product/139/520/ceiiyGdejX0E.jpg")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        try {
                            return loadPic(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        mProgressBar.setVisibility(View.GONE);
                        mImageView.setImageBitmap(bitmap);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mProgressBar.setVisibility(View.GONE);
                        Log.e("Denny", "error");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.e("Denny", "complete");
                    }
                });
    }

    private Bitmap loadPic(String url) throws IOException {
        URL mUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
        InputStream inputStream = connection.getInputStream();

        return BitmapFactory.decodeStream(inputStream);
    }
}
