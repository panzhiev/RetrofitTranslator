package com.tim.retrofittranslator;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Tim on 21.04.2017.
 */

public interface Link {

    //content://authority/path
//    https://translate.yandex.net/
// api/v1.5/tr.json/translate
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
//    Call<Object> translate(@FieldMap Map<String, String > map);
    Observable<Object> translate(@FieldMap Map<String, String> map);
}