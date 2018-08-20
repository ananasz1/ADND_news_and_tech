package com.example.vikischmideg.newsandtech.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by schmidegv on 2018. 07. 24..
 */

/**
* http://square.github.io/retrofit/
*/

public class ApiClient {


private static final String BASE_URL = "https://newsapi.org/v2/";
private static Retrofit retrofit = null;

public static Retrofit getNewsClient() {

if (null == retrofit) {
   retrofit = new Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build();
}
return retrofit;
}

}