package ando.districts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommonUtils {

    //阿里云获取地址, 不好用
    public static void getAliCloudDistricts() {
        //http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/100000_province.json
        //http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/110100_district.json
        String url = "http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/110100_district.json";

        //getProvince();
        getRequest(url);
    }

    public static String getRequest(String url) {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            //创建httpClient实例
            client = HttpClients.createDefault();
            //创建一个uri对象
            URIBuilder uriBuilder = new URIBuilder(url);
            //塞入form参数
            //uriBuilder.addParameter("account", "123");
            //uriBuilder.addParameter("password", "123");
            //创建httpGet远程连接实例,这里传入目标的网络地址
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            //设置请求头信息，鉴权(没有可忽略)
            //httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            //设置配置请求参数(没有可忽略)
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);

            //执行请求
            response = client.execute(httpGet);
            //获取Response状态码
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            //获取响应实体, 响应内容
            HttpEntity entity = response.getEntity();
            //通过EntityUtils中的toString方法将结果转换为字符串
            String str = EntityUtils.toString(entity);
            System.out.println(str);
            return str;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(response);
            closeIO(client);
        }
        return null;
    }

    public static String postRequest(String url, Map<String, Object> paramMap) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 创建httpPost远程连接实例
        HttpPost post = new HttpPost(url);
        String result = "";
        try (CloseableHttpClient closeableHttpClient = httpClientBuilder.build()) {
            // HttpEntity entity = new StringEntity(jsonDataStr);
            // 修复 POST json 导致中文乱码
            HttpEntity entity = new StringEntity(paramMap.toString(), "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");
            HttpResponse resp = closeableHttpClient.execute(post);
            try {
                InputStream respIs = resp.getEntity().getContent();
//                byte[] respBytes = IOUtils.toByteArray(respIs);
//                result = new String(respBytes, Charset.forName("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postFormRequest(String url, Map<String, Object> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例(不需要可忽略)
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置(不需要可忽略)
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        // 封装表单参数
        if (null != paramMap && paramMap.size() > 0) {
            // 以下代码使用实现类BasicNameValuePair生成NameValuePair
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                // post实例塞参数的方法
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // 服务器返回的所有信息都在HttpResponse中, httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 先取出服务器返回的状态码,如果等于200说明success
            int code = httpResponse.getStatusLine().getStatusCode();
            // 从响应对象中获取响应内容
            // EntityUtils.toString()有重载方法
            // 这个静态方法将HttpEntity转换成字符串,防止服务器返回的数据带有中文,所以在转换的时候将字符集指定成utf-8即可
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(httpResponse);
            closeIO(httpClient);
        }
        return result;
    }

    public static void reOrder(JsonArray provinceArray) {
        final JsonArray newProvinceArray = new JsonArray();
        for (JsonElement element : provinceArray) {
            JsonObject provinceObject = element.getAsJsonObject();
            JsonElement nameElement = provinceObject.get("name");
            String name = nameElement.getAsString();

        }
        //Collections.sort(provinceArray,Comparator.comparing(st -> ((JsonObject) st).get("name").getAsString()));
    }

    /**
     * 生成.json格式文件
     *
     * @param filePath "C:/xxx/xxx"
     * @param fileName "xxx"
     */
    public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
        //标记文件生成是否成功
        boolean flag = true;
        //拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";
        try {
            //保证创建一个新文件
            File file = new File(fullPath);
            //如果父目录不存在，创建父目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //如果已存在,删除旧文件
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            //将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    public static void closeIO(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}