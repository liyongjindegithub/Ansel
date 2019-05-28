package com.example.okutils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 代码复用性强
 * 单例的类；构造私有化，自行实例化，提供公开的方法
 */
public class OKUtils {
   private OkHttpClient client;
   private OKUtils(){
       client = new OkHttpClient.Builder()
               .callTimeout(5, TimeUnit.SECONDS)
               .connectTimeout(5, TimeUnit.SECONDS)
               .build();
   }
   private static OKUtils okUtils = new OKUtils();

   public static OKUtils getInstance(){
       return okUtils;
   }

   //get请求
    public void doGet(String url, final GetValue getValue){
        Request build = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(build);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getValue.error(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getValue.success(response.body().string());
            }
        });
    }

    //post请求
    public void doPost(String url, final GetValue getValue, String[] key, String[] value){
        FormBody.Builder frombody = new FormBody.Builder();

        for (int i = 0;i<key.length;i++){
            frombody.add(key[i],value[i]);
        }
        FormBody build1 = frombody.build();
        Request build = new Request.Builder()
                .post(build1)
                .url(url)
                .build();
        Call call = client.newCall(build);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getValue.error(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getValue.success(response.body().string());
            }
        });
    }

    public void download(String url, final GetValue getValue){
        Request build = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(build);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getValue.error(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/Music/xiazai.txt");
                int len = 0;
                byte[] bytes = new byte[1024];
                while((len = inputStream.read(bytes))!= -1){
                    fileOutputStream.write(bytes,0,len);
                }
                fileOutputStream.close();

                getValue.success("下载成功");
            }
        });
    }



    interface GetValue{
       public void success(String string);
       public void error(String string);
    }

}
