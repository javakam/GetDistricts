package ando.districts.pojo;

import java.util.List;

/**
 * Auto-generated: 2022-09-28 10:50:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class GdDistrictsEntity {

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