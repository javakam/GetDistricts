package ando.districts;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 使用高德API获取全国行政区域信息
 *
 * @author ChangBao
 * @date 2022年9月28日 16:09:07
 */
public class GetDistricts {

    //https://lbs.amap.com/api/webservice/guide/api/district/
    public static final String URL = "https://restapi.amap.com/v3/config/district?subdistrict=3&key=你的Key";
    public static final String FILE_PATH = ".";//当前项目目录
    public static final String FILE_NAME = "province";

    public static void main(String[] args) {
        GetDistricts.getGaoDeAllDistricts();
    }

    /**
     * 高德接口可以一次性直接获取全国的行政区域信息
     */
    public static void getGaoDeAllDistricts() {
        final String responseStr = Utils.getRequest(URL);
        Gson gson = new Gson();
        GdDistrictsWrapper addressWrapper = gson.fromJson(responseStr, GdDistrictsWrapper.class);
        List<GdDistrictsWrapper.GdDistrictsEntity> districtsWrapper = addressWrapper.getDistricts();
        if (districtsWrapper != null && !districtsWrapper.isEmpty()) {
            List<GdDistrictsWrapper.GdDistrictsEntity> provinceDistricts = districtsWrapper.get(0).getDistricts();
            write2Local(provinceDistricts);
        }
    }

    //Json原始样本_高德.json -> Json目标样本.json
    private static void write2Local(List<GdDistrictsWrapper.GdDistrictsEntity> provinceDistricts) {
        System.out.println("全国省级行政单位数量: " + provinceDistricts.size());
        final JsonArray provinceArray = new JsonArray();
        final List<JsonObject> provinceSortArray = new ArrayList<>();
        for (GdDistrictsWrapper.GdDistrictsEntity province : provinceDistricts) {
            final JsonObject provinceObject = new JsonObject();
            provinceObject.addProperty("adcode", province.getAdcode());//用于省级行政单位排序
            provinceObject.addProperty("name", province.getName());//北京市

            final JsonArray cityArray = new JsonArray();
            final List<GdDistrictsWrapper.GdDistrictsEntity> cityDistricts = province.getDistricts();
            if (cityDistricts != null && !cityDistricts.isEmpty()) {
                for (GdDistrictsWrapper.GdDistrictsEntity city : cityDistricts) {
                    final JsonObject cityObject = new JsonObject();
                    //cityObject.addProperty("adcode", city.getAdcode());//城区之间可以根据此字段排序
                    cityObject.addProperty("name", city.getName());//北京城区

                    final JsonArray districtArray = new JsonArray();
                    final List<GdDistrictsWrapper.GdDistrictsEntity> districtDistricts = city.getDistricts();
                    if (districtDistricts != null && !districtDistricts.isEmpty()) {
                        for (GdDistrictsWrapper.GdDistrictsEntity district : districtDistricts) {
                            districtArray.add(district.getName());//怀柔区
                        }
                    }

                    cityObject.add("area", districtArray);
                    cityArray.add(cityObject);
                }
            }
            provinceObject.add("city", cityArray);
            provinceSortArray.add(provinceObject);//provinceArray.add(provinceObject);
        }
        //高德的地址数据是 河南省,广东省... 顺序的, 需要改成 北京,天津这样的顺序, 用 adcode 从小到大排序即可
        provinceSortArray.sort(Comparator.comparing(st -> st.get("adcode").getAsString()));
        for (JsonObject object : provinceSortArray) {
            provinceArray.add(object);//可以使用 Iterator 移除 adcode, 就不弄了
        }

        boolean flag = Utils.createJsonFile(provinceArray.toString(), FILE_PATH, FILE_NAME);
        if (flag) {
            System.out.println("Json文件创建成功!");
        } else {
            System.out.println("写入失败!");
        }
    }

    /////////////////////////////// 关键字查询
    public static final String URL_FORMAT = "https://restapi.amap.com/v3/config/district?keywords=%s&subdistrict=%d&key=%s";

    private static void getAddress(String keywords, int subdistrict, String key) {
        final String url = String.format(Locale.getDefault(), URL_FORMAT, keywords, subdistrict, key);
        Utils.getRequest(url);
    }

    /**
     * Auto-generated: 2022-09-28 10:50:25
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public static class GdDistrictsWrapper {
        private String status;
        private String info;
        private String infocode;
        private String count;
        private List<GdDistrictsEntity> districts;

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }

        public void setInfocode(String infocode) {
            this.infocode = infocode;
        }

        public String getInfocode() {
            return infocode;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getCount() {
            return count;
        }

        public void setDistricts(List<GdDistrictsEntity> districts) {
            this.districts = districts;
        }

        public List<GdDistrictsEntity> getDistricts() {
            return districts;
        }

        /**
         * Auto-generated: 2022-09-28 10:50:25
         *
         * @author bejson.com (i@bejson.com)
         * @website http://www.bejson.com/java2pojo/
         */
        public static class GdDistrictsEntity {

            private Object citycode;// 外层是 List<String> , 内层是 String
            private String adcode;
            private String name;
            private String center;
            private String level;
            private List<GdDistrictsEntity> districts;

            public void setCitycode(Object citycode) {
                this.citycode = citycode;
            }

            public Object getCitycode() {
                return citycode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setCenter(String center) {
                this.center = center;
            }

            public String getCenter() {
                return center;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getLevel() {
                return level;
            }

            public void setDistricts(List<GdDistrictsEntity> districts) {
                this.districts = districts;
            }

            public List<GdDistrictsEntity> getDistricts() {
                return districts;
            }

        }
    }
}