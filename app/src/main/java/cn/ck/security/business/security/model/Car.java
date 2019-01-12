package cn.ck.security.business.security.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author chengkun
 * @since 2019/1/12 19:45
 */
public class Car {
    /**
     * id : 11
     * name : pig
     * department : CS
     * carNumber : aq10000009783
     * phone : 13113313779
     * relation:"本人"
     */

    private int id;
    private String name;
    private String department;
    @SerializedName("car_number")
    private String carNumber;
    private String phone;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    private String relation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
