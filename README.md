# 全国行政区域信息

## 方案

- 高德    https://lbs.amap.com/api/webservice/guide/api/district/

- 阿里云  https://blog.csdn.net/jimolangyaleng/article/details/85596748

> 高德需要审核密钥, 阿里云这个直接浏览器 Get 就可以。

#### 高德[推荐]

> 高德接口可以一次性直接获取全国的行政区域信息, 非常好用!

1. `申请Web服务API类型KEY`时`创建应用`需要选择`Web服务`而不是`Web端(JS API)`
2. 先下载`城市编码表`: `Web服务 API 相关下载`👉 <https://lbs.amap.com/api/webservice/download>
   , 文件是一个`xlsx`, 设置筛选`adcode`条件为`0000`, 结果是`省份`的列表, 配合`subdistrict=3`, 就可以实现一次请求就能够获取到全国各地最新的`行政区域信息`
3. `https://restapi.amap.com/v3/config/district?keywords=北京&subdistrict=3&extensions=base&key=xxx`
4. `GetDistricts.java`中生成的`province.json`是用于安卓项目 <https://github.com/javakam/PickerView> , 可以根据你的需要自行调整数据结构。

#### 阿里云中国省市县数据接口

> 优点还是有的, 比如简单的查询一些地址信息还是很方便的, 毕竟不用登录官网不用注册不用创建应用不用申请key~

- 省（全国各省）

http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/100000_province.json

- 市(以安徽为例)

http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/340000_city.json

- 县

http://datavmap-public.oss-cn-hangzhou.aliyuncs.com/areas/csv/341700_district.json

> 参考: https://blog.csdn.net/jimolangyaleng/article/details/85596748

#### HttpClient 用法

<https://blog.csdn.net/WYP123456L/article/details/121989884>