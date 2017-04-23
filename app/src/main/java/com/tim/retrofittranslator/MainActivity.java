package com.tim.retrofittranslator;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivity";
    private EditText mEditText_first, mEditText_second;
    private Button mButtonRuEn, mButtonEnRu, mButtonRuFr, mButtonFrRu, mButtonRuDe, mButtonDeRu, mButtonRuPl, mButtonPlRu, mButtonRuZh, mButtonZhRu;
    final private String URL = "https://translate.yandex.net";
    final private String KEY = "trnsl.1.1.20160719T224220Z.24f42814f637f644.dc9cbf9b0dec16722dd04d18cbbac6d6d1974d74";

    Gson gson = new GsonBuilder().create();

    RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxAdapter)
            .build();

    private Link linkInterface = retrofit.create(Link.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initialize();
    }

    private void initialize() {
        mButtonRuEn = (Button) findViewById(R.id.buttonRuEn);
        mButtonEnRu = (Button) findViewById(R.id.buttonEnRu);
        mButtonRuFr = (Button) findViewById(R.id.buttonRuFr);
        mButtonFrRu = (Button) findViewById(R.id.buttonFrRu);
        mButtonRuDe = (Button) findViewById(R.id.buttonRuDe);
        mButtonDeRu = (Button) findViewById(R.id.buttonDeRu);
        mButtonRuPl = (Button) findViewById(R.id.buttonRuPl);
        mButtonPlRu = (Button) findViewById(R.id.buttonPlRu);
        mButtonRuZh = (Button) findViewById(R.id.buttonRuZh);
        mButtonZhRu = (Button) findViewById(R.id.buttonZhRu);
        mButtonRuEn.setOnClickListener(this);
        mButtonEnRu.setOnClickListener(this);
        mButtonRuFr.setOnClickListener(this);
        mButtonFrRu.setOnClickListener(this);
        mButtonRuDe.setOnClickListener(this);
        mButtonDeRu.setOnClickListener(this);
        mButtonRuPl.setOnClickListener(this);
        mButtonPlRu.setOnClickListener(this);
        mButtonRuZh.setOnClickListener(this);
        mButtonZhRu.setOnClickListener(this);
        mEditText_first = (EditText) findViewById(R.id.et1);
        mEditText_second = (EditText) findViewById(R.id.et2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRuEn:
                forwardTranslation(TranslationConstants.ruEn);
                break;
            case R.id.buttonRuFr:
                forwardTranslation(TranslationConstants.ruFr);
                break;
            case R.id.buttonRuDe:
                forwardTranslation(TranslationConstants.ruDe);
                break;
            case R.id.buttonRuPl:
                forwardTranslation(TranslationConstants.ruPl);
                break;
            case R.id.buttonRuZh:
                forwardTranslation(TranslationConstants.ruZh);
                break;
            case R.id.buttonEnRu:
                backTranslation(TranslationConstants.enRu);
                break;
            case R.id.buttonFrRu:
                backTranslation(TranslationConstants.frRu);
                break;
            case R.id.buttonDeRu:
                backTranslation(TranslationConstants.deRu);
                break;
            case R.id.buttonPlRu:
                backTranslation(TranslationConstants.plRu);
                break;
            case R.id.buttonZhRu:
                backTranslation(TranslationConstants.zhRu);
                break;
//                Call<Object> call = linkInterface.translate(mapjson);
//                try {
//                    Response<Object> res = call.execute();
//
//                    Map<String, String> map = gson.fromJson(res.body().toString(), Map.class);
//
//                    for (Map.Entry e : map.entrySet()) {
//                        if (e.getKey().equals("text")) {
//                            mEditText_second.setText(e.getValue().toString());
//                        }
//                        //System.out.println(e.getKey()+" "+e.getValue());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            default:
                break;
        }
    }

    private void forwardTranslation(String languageFromTo) {
        Map<String, String> mapjson = new HashMap<>();
        mapjson.put("key", KEY);
        mapjson.put("text", mEditText_first.getText().toString());
        mapjson.put("lang", languageFromTo);

        Observable<Object> translater = linkInterface.translate(mapjson);

        translater.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
//                                Log.d(LOG_TAG, " onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            int code = response.code();
                            Log.d(LOG_TAG, String.valueOf(code));
                        }
//                                Log.d(LOG_TAG, "onError()");
//                                Log.d(LOG_TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Object object) {
                        Log.d(LOG_TAG, object.toString() + " response TRANSLATER");

                        Map map = gson.fromJson(object.toString(), Map.class);
                        String translation = map.get("text").toString();
                        translation = translation.replace("]", "").replace("[", "");
                        mEditText_second.setText(translation);
//                                for (Map.Entry e : (object.entrySet()) {
//                                    if (e.getKey().equals("text")) {
//                                        translated.setText(e.getValue().toString());
//                                    }
//                                }
                    }
                });
    }

    private void backTranslation(String languageFromTo) {
        Map<String, String> mapjson = new HashMap<>();
        mapjson.put("key", KEY);
        mapjson.put("text", mEditText_second.getText().toString());
        mapjson.put("lang", languageFromTo);

        Observable<Object> trasleter = linkInterface.translate(mapjson);

        trasleter.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, " onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "onError()");
                        Log.d(LOG_TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Object object) {
                        Log.d(LOG_TAG, object.toString() + " response TRANSLATER");

                        Map map = gson.fromJson(object.toString(), Map.class);
                        String translation = map.get("text").toString();
                        translation = translation.replace("]", "").replace("[", "");
                        mEditText_first.setText(translation);
//                                for (Map.Entry e : (object.entrySet()) {
//                                    if (e.getKey().equals("text")) {
//                                        translated.setText(e.getValue().toString());
//                                    }
//                                }
                    }
                });
    }
}
