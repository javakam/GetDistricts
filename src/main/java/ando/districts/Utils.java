package ando.districts;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class Utils {

    //阿里云获取地址, 不好用
    public static void getAliCloudDistricts() {
        //http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/100000_province.json
        //http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/110100_district.json
        String url = "http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/110100_district.json";

        //getProvince();
        getRequest(url);
    }

    //HttpClient https://blog.csdn.net/WYP123456L/article/details/121989884
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