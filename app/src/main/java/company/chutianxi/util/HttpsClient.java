package company.chutianxi.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpsClient {
    public static String HttpGet(String address,Map<String,String> params)
    {
        String responseStr = null;
        String requestStr = null;
        String res = null;
        HttpsURLConnection con = null;
        StringBuilder tmp = new StringBuilder();
        Iterator<Map.Entry<String,String>> entries = params.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<String,String> entry = entries.next();
            tmp.append(entry.getKey());
            tmp.append("=");
            tmp.append(entry.getValue());
            tmp.append("&");
        }
        tmp.deleteCharAt(tmp.length()-1);
        requestStr = address + tmp.toString();
        Log.d("info",requestStr);
        try {
            URL url = new URL(requestStr);
            con = (HttpsURLConnection)url.openConnection();
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            TrustManager[] tm = {new MyX509TrustManager()};
            sslcontext.init(null,tm, new java.security.SecureRandom());
            SSLSocketFactory ssf =	sslcontext.getSocketFactory();
            con.setRequestMethod("GET");
            con.setConnectTimeout(8000);
            con.setReadTimeout(8000);
            con.setHostnameVerifier(DO_NOT_VERIFY);
            con.setDoInput(true);
            con.setSSLSocketFactory(ssf);
            con.connect();
/*	            con.connect();此方法无用
	            if(null!=requestStr)
	            {
	            	OutputStream os = con.getOutputStream();
	            	os.write(requestStr.getBytes("utf-8"));
	            	os.close();
	            }*/
            InputStream in = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                response.append(str);
            }
            responseStr = response.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(con!=null)
                con.disconnect();
        }
        try {
            res = new String(responseStr.getBytes(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }
    /**
     * 设置不验证主机是必须的
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}

