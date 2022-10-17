package uz.example.less67_java_retrofit_2api.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeResp {
    @SerializedName("id")
    int id;
    @SerializedName("employee_name")
    String employee_name;
    @SerializedName("employee_salary")
    int employee_salary;
    @SerializedName("employee_age")
    int employee_age;

    public int getId() {
        return id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public int getEmployee_salary() {
        return employee_salary;
    }

    public int getEmployee_age() {
        return employee_age;
    }

    @Override
    public String toString() {
        return "EmployeeResp{" +
                "id=" + id +
                ", employee_name='" + employee_name + '\'' +
                ", employee_salary=" + employee_salary +
                ", employee_age=" + employee_age +
                '}';
    }
}
