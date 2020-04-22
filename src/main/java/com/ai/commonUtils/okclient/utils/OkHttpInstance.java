package com.ai.commonUtils.okclient.utils;

import okhttp3.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author: lixuejun
 * @date: Create in 2019/10/21 下午2:44
 * @description: 单例客户端
 */
public class OkHttpInstance {
    private OkHttpClient client;
    private OkHttpInstance() {
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
            HostnameVerifier doNotVerify = (hostname, session) -> true;
            this.client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), xtm)
                    .hostnameVerifier(doNotVerify)
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        if (this.client == null) {
            this.client = new OkHttpClient();
        }
    }

    public static OkHttpClient getClient() {
        return ClientHolder.HTTP_INSTANCE.client;
    }

    private static class ClientHolder {
        private static final OkHttpInstance HTTP_INSTANCE = new OkHttpInstance();
    }


}
