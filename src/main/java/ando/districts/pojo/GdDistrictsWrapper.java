package ando.districts.pojo;

import java.util.List;

/**
 * Auto-generated: 2022-09-28 10:50:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class GdDistrictsWrapper {
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
}