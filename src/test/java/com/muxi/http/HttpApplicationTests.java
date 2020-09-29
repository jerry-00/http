package com.muxi.http;

import com.alibaba.fastjson.JSON;
import com.muxi.http.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HttpClient 发送方
 *
 * @author muxi
 * @date 2020/9/28
 */
@SpringBootTest
class HttpApplicationTests {

    /**
     * Get无参
     */
    @Test
    void get() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:8600/get");
        // 响应模型
        CloseableHttpResponse httpResponse = null;

        try {
            // 由客户端执行(发送)Get请求
            httpResponse = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                // FAQ: 设置编码，防止乱码
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * get有参
     * url后面追加参数
     */
    @Test
    void getByParams() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 参数列表
        StringBuffer params = new StringBuffer();
        try {
            params.append("name=" + URLEncoder.encode("lily", "utf-8"));
            params.append("&");
            params.append("age=30");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:8600/getByParams?" + params);
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // Socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();
            // 将上面的配置信息 运用到这个Get请求里
            httpGet.setConfig(requestConfig);

            // 由客户端执行(发送)Get请求
            httpResponse = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * Post无参
     */
    @Test
    void post() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:8600/post");
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            // 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * Post有参（普通参数）
     */
    @Test
    void postByParams() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 参数列表
        StringBuffer params = new StringBuffer();
        try {
            params.append("name=" + URLEncoder.encode("thomas", "utf-8"));
            params.append("&");
            params.append("age=30");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:8600/postByParams?" + params);
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            // 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * Post有参（对象）
     */
    @Test
    void postByObj() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:8600/postByObj");
        User user = new User();
        user.setName("Sophia");
        user.setAge(28);
        user.setSex("female");
        user.setMotto("Take is ability, give up is realm");
        // 我这里利用阿里的fastjson，将Object转换为json字符串;
        // (需要导入com.alibaba.fastjson.JSON包)
        String json = JSON.toJSONString(user);
        StringEntity entity = new StringEntity(json, "UTF-8");
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            // 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * Post有参（普通参数+对象参数）
     */
    @Test
    void postByParamsObj() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 参数
        URI uri = null;
        try {
            // 将参数放入键值对类NameValuePair中,再放入集合中
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("flag", "4"));
            params.add(new BasicNameValuePair("meaning", "what"));
            // 设置uri信息,并将参数集合放入uri;
            // 注:这里也支持一个键值对一个键值对地往里面放setParameter(String key, String value)
            uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8600)
                    .setPath("/postByParamsObj").setParameters(params).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // 创建Post请求
        HttpPost httpPost = new HttpPost(uri);
        User user = new User();
        user.setName("Sophia");
        user.setAge(28);
        user.setSex("female");
        user.setMotto("Take is ability, give up is realm");
        // 我这里利用阿里的fastjson，将Object转换为json字符串;
        // (需要导入com.alibaba.fastjson.JSON包)
        String json = JSON.toJSONString(user);
        StringEntity entity = new StringEntity(json, "UTF-8");
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            // 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    @Test
    void https() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = getHttpClient(true);
        HttpGet httpGet = new HttpGet("https://localhost:8500/https");
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            // 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * Post有参（表单请求）
     */
    @Test
    void postForm() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 参数列表
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("name", "jone"));
        params.add(new BasicNameValuePair("age", "50"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:8600/postForm");
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(formEntity);
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            // 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + httpEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(httpEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * 发送文件
     *
     * multipart/form-data传递文件(及相关信息)
     * 注:如果想要灵活方便的传输文件的话，
     * 除了引入org.apache.httpcomponents基本的httpclient依赖外
     * 再额外引入org.apache.httpcomponents的httpmime依赖。
     * 追注:即便不引入httpmime依赖，也是能传输文件的，不过功能不够强大。
     */
    @Test
    void files() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:8600/files");
        // 响应模型
        CloseableHttpResponse httpResponse = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            String fileskey = "files";
            File f1 = new File("D:\\white.png");
            multipartEntityBuilder.addBinaryBody(fileskey, f1);
            // 第二个文件(多个文件的话，使用同一个key就行，后端用数组或集合进行接收即可)
            File f2 = new File("D:\\wrapper.png");
            // 防止服务端收到的文件名乱码。 我们这里可以先将文D件名URLEncode，然后服务端拿到文件名时在URLDecode。就能避免乱码问题。
            // 文件名其实是放在请求头的Content-Disposition里面进行传输的，如其值为form-data; name="files"; filename="头像.jpg"
            multipartEntityBuilder.addBinaryBody(fileskey, f2, ContentType.DEFAULT_BINARY, URLEncoder.encode(f2.getName(), "utf-8"));
            // 其它参数(注:自定义contentType，设置UTF-8是为了防止服务端拿到的参数出现乱码)
            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
            multipartEntityBuilder.addTextBody("name", "成吉思汗", contentType);
            multipartEntityBuilder.addTextBody("age", "20", contentType);

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);
            // 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = httpResponse.getEntity();
            System.out.println("响应状态为:" + httpResponse.getStatusLine());
            if (httpEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                System.out.println("响应内容为:" + responseStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, httpResponse);
        }
    }

    /**
     * 发送流
     */
    @Test
    public void dataFlow() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:8600/dataFlow?name=上官云");
        CloseableHttpResponse response = null;
        try {
            InputStream is = new ByteArrayInputStream("数据流~".getBytes());
            InputStreamEntity ise = new InputStreamEntity(is);
            httpPost.setEntity(ise);

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("HTTPS响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("HTTPS响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                System.out.println("HTTPS响应内容为:" + responseStr);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            this.close(httpClient, response);
        }
    }

    /**
     * 根据是否是https请求，获取HttpClient客户端
     * <p>
     * TODO 本人这里没有进行完美封装。对于 校不校验校验证书的选择，本人这里是写死
     * 在代码里面的，你们在使用时，可以灵活二次封装。
     * <p>
     * 提示: 此工具类的封装、相关客户端、服务端证书的生成，可参考我的这篇博客:
     * <linked>https://blog.csdn.net/justry_deng/article/details/91569132</linked>
     *
     * @param isHttps 是否是HTTPS请求
     * @return HttpClient实例
     */
    private CloseableHttpClient getHttpClient(boolean isHttps) {
        CloseableHttpClient httpClient;
        if (isHttps) {
            SSLConnectionSocketFactory sslSocketFactory;
            try {
                // 如果不作证书校验的话
//                sslSocketFactory = getSocketFactory(false, null, null);

                // 如果需要证书检验的话
                // 证书
//                InputStream ca = this.getClass().getClassLoader().getResourceAsStream("tyb.crt");
                InputStream ca = this.getClass().getClassLoader().getResourceAsStream("https.crt");
                // 证书的别名，即:key。 注:cAalias只需要保证唯一即可，不过推荐使用生成keystore时使用的别名。
                String cAalias = System.currentTimeMillis() + "" + new SecureRandom().nextInt(1000);
                sslSocketFactory = getSocketFactory(true, ca, cAalias);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
            return httpClient;
        }
        httpClient = HttpClientBuilder.create().build();
        return httpClient;
    }

    /**
     * HTTPS辅助方法, 为HTTPS请求 创建SSLSocketFactory实例、TrustManager实例
     *
     * @param needVerifyCa  是否需要检验CA证书(即:是否需要检验服务器的身份)
     * @param caInputStream CA证书。(若不需要检验证书，那么此处传null即可)
     * @param cAalias       别名。(若不需要检验证书，那么此处传null即可)
     *                      注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
     * @return SSLConnectionSocketFactory实例
     * @throws NoSuchAlgorithmException 异常信息
     * @throws CertificateException     异常信息
     * @throws KeyStoreException        异常信息
     * @throws IOException              异常信息
     * @throws KeyManagementException   异常信息
     */
    private static SSLConnectionSocketFactory getSocketFactory(boolean needVerifyCa, InputStream caInputStream, String cAalias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException, KeyManagementException {
        X509TrustManager x509TrustManager;
        // https请求，需要校验证书
        if (needVerifyCa) {
            KeyStore keyStore = getKeyStore(caInputStream, cAalias);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            x509TrustManager = (X509TrustManager) trustManagers[0];
            // 这里传TLS或SSL其实都可以的
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            //  只忽略域名验证码 NoopHostnameVerifier.INSTANCE
            return new SSLConnectionSocketFactory(sslContext);
//            return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        }
        // https请求，不作证书校验
        x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // 不验证
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        //  只忽略域名验证码 NoopHostnameVerifier.INSTANCE
//        return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        return new SSLConnectionSocketFactory(sslContext);
    }

    /**
     * 获取(密钥及证书)仓库
     * 注:该仓库用于存放 密钥以及证书
     *
     * @param caInputStream CA证书(此证书应由要访问的服务端提供)
     * @param cAalias       别名
     *                      注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
     * @return 密钥、证书 仓库
     * @throws KeyStoreException        异常信息
     * @throws CertificateException     异常信息
     * @throws IOException              异常信息
     * @throws NoSuchAlgorithmException 异常信息
     */
    private static KeyStore getKeyStore(InputStream caInputStream, String cAalias)
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        // 证书工厂
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        // 秘钥仓库
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        keyStore.setCertificateEntry(cAalias, certificateFactory.generateCertificate(caInputStream));
        return keyStore;
    }

    /**
     * 释放资源
     */
    private void close(CloseableHttpClient httpClient, CloseableHttpResponse httpResponse) {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
