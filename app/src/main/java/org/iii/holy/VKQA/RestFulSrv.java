package org.iii.holy.VKQA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.iii.holy.model.LoginItem;
import org.iii.holy.model.SimplifyQuestion;
import org.iii.holy.model.VoteItem;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;


/**
 * Created by Holy on 2016/5/18.
 */
public class RestFulSrv extends AsyncTask<String, Void, Map<String, Object>> {

    private static volatile List<Cookie> cookies_ = new ArrayList<Cookie>();
    OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookies_.addAll(cookies);
                }
                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    return cookies_;
                }
            }) .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private Context mContext;

    public RestFulSrv(Context context) {
        mContext = context;
    }

    public void ClearCookie() {
        cookies_.clear();
    }

    @Override
    protected Map<String, Object> doInBackground(String... params) {

        Request request;
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        if (params.length > 1) {
            switch (params[1]) {
                case "login":
                    ClearCookie();
                    LoginItem usrLogin = gson.fromJson(params[2], LoginItem.class);

                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("name", usrLogin.getName());
                    builder.add("token", usrLogin.getToken());
                    RequestBody body = builder.build();

                    request = new Request.Builder()
                            .url(params[0])
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .post(body)
                            .build();
                    break;
                case "ask":
                    SimplifyQuestion simpleQ = gson.fromJson(params[2], SimplifyQuestion.class);
                    builder = new FormBody.Builder();
                    builder.add("title", simpleQ.getTitle());
                    builder.add("description", simpleQ.getContent());
                    builder.add("tagNames", simpleQ.getTag());
                    body = builder.build();
                    request = new Request.Builder()
                            .url(params[0])
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .post(body)
                            .build();
                    break;
                case "answer":
                    builder = new FormBody.Builder();
                    builder.add("description", params[2]);
                    body = builder.build();
                    request = new Request.Builder()
                            .url(params[0])
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .post(body)
                            .build();

                    break;
                case "vote":
                    VoteItem vItem = gson.fromJson(params[2], VoteItem.class);
                    builder = new FormBody.Builder();
                    String sQueryPara = "onWhat=" + vItem.getOnWhat() + "&" + "onWhatId=" + vItem.getOnWhatId();
                    //http://localhost/api/voteup?onWhat=question&onWhatId=257
                    body = builder.build();
                    request = new Request.Builder()
                            .url(params[0] + sQueryPara)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .post(body)
                            .build();
                    break;
                default:
                    Request.Builder rBuilder = new Request.Builder();
                    rBuilder.url(params[0]);
                    request = rBuilder.build();

            }

        } else {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            request = builder.build();

        }

        try {
            Response response = client.newCall(request).execute();


            Map<String, Object> mapResult = new HashMap<>();
            //which request's response : response.request().url()
            if (response.body().contentType().subtype().equals("json"))
            //return response.body().string();
            {
                mapResult.put("fromAPI", response.request().url());
                mapResult.put("body", response.body().string().replace("\n", ""));
                return mapResult;
            } else
                return null;
        } catch (Exception e) {
            Map<String, Object> mapResult = new HashMap<>();
            e.printStackTrace();
            mapResult.put("fromAPI","Error");
            mapResult.put("body", "網路連線異常");
            return mapResult;
        }

    }

    @Override
    protected void onPostExecute(Map<String, Object> s) {
        super.onPostExecute(s);
        if (s != null) {
            String sAPIurl = s.get("fromAPI").toString();
            if (sAPIurl.contains("/api/q/list/")) {
                ((QAActivity) mContext).freshQpageUI(s);
            }
            if (sAPIurl.contains("/api/karmaRule")) {
                ((QAActivity) mContext).setKarma(s);
            }
            if (sAPIurl.contains("/api/qDetail/get/")) {
                ((QAActivity) mContext).freshApageUI(s);
            }
            if (sAPIurl.contains("/api/auth")) {
                ((QAActivity) mContext).setAuth(s);
            }
            if (sAPIurl.contains("/api/logout")) {
                ((QAActivity) mContext).freshLogoutUI(s);
            }
            if (sAPIurl.contains("/api/q/new") || sAPIurl.contains("api/q/newAnswer/")) {
                ((QAActivity) mContext).RefreshQA();
            }
            if (sAPIurl.contains("Error"))
            {
                ((QAActivity) mContext).connectError(s);
            }


        }

    }

    public Map<String, Object> get(String url) {

        RestFulSrv handler = new RestFulSrv(mContext);
        Map<String, Object> result = null;
        try {
            result = handler.execute(url).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    Map<String, Object> post(String url, String type, String json) {

        RestFulSrv handler = new RestFulSrv(mContext);
        Map<String, Object> result = null;
        try {
            result = handler.execute(url, type, json).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


}

