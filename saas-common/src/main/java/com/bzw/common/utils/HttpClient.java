package com.bzw.common.utils;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * HttpClient工具类
 *
 * @author yanbin
 */
public class HttpClient {

    private static final int TRY_COUNT = 3;

    private static CloseableHttpClient httpClient = null;

    private static final Object SYNC_LOCK = new Object();

    private static final Logger logger = Logger.getLogger(HttpClient.class);

    static CloseableHttpClient getHttpClient(String url) {
        String dot = ":";
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(dot)) {
            String[] arr = hostname.split(dot);
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        if (httpClient == null) {
            synchronized (SYNC_LOCK) {
                if (httpClient == null) {
                    httpClient = createHttpClient(200, 40, 100, hostname, port);
                }
            }
        }
        return httpClient;
    }

    private static CloseableHttpClient createHttpClient(int maxTotal,
                                                        int maxPerRoute, int maxRoute, String hostname, int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                // 如果已经重试了3次，就放弃
                if (executionCount >= TRY_COUNT) {
                    return false;
                }
                // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                // 不要重试SSL握手异常
                if (exception instanceof SSLHandshakeException) {
                    return false;
                }
                // 超时
                if (exception instanceof InterruptedIOException) {
                    return false;
                }
                // 目标服务器不可达
                if (exception instanceof UnknownHostException) {
                    return false;
                }
                // 连接被拒绝
                if (exception instanceof ConnectTimeoutException) {
                    return false;
                }
                // SSL握手异常
                if (exception instanceof SSLException) {
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                return !(request instanceof HttpEntityEnclosingRequest);
            }
        };

        return HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();
    }

    private static void setPostJson(HttpPost httpost,
                                    String content) {
        //解决中文乱码问题
        StringEntity entity = new StringEntity(content, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpost.setEntity(entity);
    }


    private static void setPostParams(HttpPost httpost,
                                      Map<String, Object> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e, e);
        }
    }

    public static String post(String url, Map<String, Object> params) {
        HttpPost httppost = new HttpPost(url);
        setPostParams(httppost, params);
        CloseableHttpResponse response = null;
        PostHttp postHttp = new PostHttp(url, httppost, response).invoke();
        if (postHttp.is()) {
            return postHttp.getResult();
        }
        return null;
    }

    public static String post(String url, Map<String, Object> params, Map<String, String> headers) {
        HttpPost httppost = new HttpPost(url);
        if (null != headers) {
            headers.forEach(httppost::addHeader);
        }
        setPostParams(httppost, params);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost,
                    HttpClientContext.create());
            return httpResult(response);
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
        return null;
    }

    public static byte[] postJson(String url, String content) {
        HttpPost httppost = new HttpPost(url);
        setPostJson(httppost, content);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost,
                    HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            return EntityUtils.toByteArray(entity);
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
        return null;
    }

    public static String postJsonStr(String url, String content) {
        HttpPost httppost = new HttpPost(url);
        setPostJson(httppost, content);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost,
                    HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
        return null;
    }

    private static String httpResult (CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "utf-8");
        EntityUtils.consume(entity);
        return result;
    }

    public static String get(String url) {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httpget,
                    HttpClientContext.create());
            return httpResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
        return null;
    }

    static class GetRunnable implements Runnable {
        private CountDownLatch countDownLatch;
        private String url;

        public GetRunnable(String url, CountDownLatch countDownLatch) {
            this.url = url;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                System.out.println(HttpClient.get(url));
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    private static class PostHttp {
        private boolean myResult;
        private String url;
        private HttpPost httppost;
        private CloseableHttpResponse response;
        private String result;

        PostHttp(String url, HttpPost httppost, CloseableHttpResponse response) {
            this.url = url;
            this.httppost = httppost;
            this.response = response;
        }

        boolean is() {
            return myResult;
        }

        public String getResult() {
            return result;
        }

        PostHttp invoke() {
            try {
                response = getHttpClient(url).execute(httppost,
                        HttpClientContext.create());
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                myResult = true;
                return this;
            } catch (Exception e) {
                logger.error(e, e);
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    logger.error(e, e);
                }
            }
            myResult = false;
            return this;
        }
    }
}