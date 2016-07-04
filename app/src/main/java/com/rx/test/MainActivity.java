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
import rx.Subscriber;
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
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // setImage();
        //createObservable();
        justObservable();
    }

    private void justObservable() {
        Observable.just("hello rx")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("Denny", s);
                    }
                });
        Observable.just(1).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

            }
        });

    }

    private void createObservable() {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello rx");
                subscriber.onCompleted();
            }
        });
        Observable observableLambda = Observable.create((subscriber) -> {
            subscriber.onNext("hello rx lambda");
            subscriber.onCompleted();
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d("Denny", s);
            }
        };
        observable.subscribe(subscriber);
        observableLambda.subscribe(subscriber);
    }

    private void setImage() {
        Observable.just("http://img2.zol.com.cn/product/139/520/ceiiyGdejX0E.jpg")
                .map(s -> {
                    try {
                        return loadPic(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Bitmap bitmap) -> {
                    mProgressBar.setVisibility(View.GONE);
                    mImageView.setImageBitmap(bitmap);
                }, throwable -> {
                    mProgressBar.setVisibility(View.GONE);
                    Log.e("Denny", "error");
                }, () -> Log.e("Denny", "complete"));
    }

    private Bitmap loadPic(String url) throws IOException {
        URL mUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
        InputStream inputStream = connection.getInputStream();

        return BitmapFactory.decodeStream(inputStream);
    }
}
