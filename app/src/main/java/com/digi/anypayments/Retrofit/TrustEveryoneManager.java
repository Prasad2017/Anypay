package com.digi.anypayments.Retrofit;

import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TrustEveryoneManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] arg0, String arg1){}
    public void checkServerTrusted(X509Certificate[] arg0, String arg1){}
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}