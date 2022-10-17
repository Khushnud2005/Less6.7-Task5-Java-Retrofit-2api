package uz.example.less67_java_retrofit_2api.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import uz.example.less67_java_retrofit_2api.model.BaseModel;
import uz.example.less67_java_retrofit_2api.model.Employee;
import uz.example.less67_java_retrofit_2api.model.EmployeeResp;

public interface EmployeeService {

    @Headers(
            "Content-type:application/json"
    )

    @GET("employees")
    Call<BaseModel<ArrayList<EmployeeResp>>> listEmployee();

    @GET("employee/{id}")
    Call<BaseModel<EmployeeResp>> singleEmployee(@Path("id") int id);

    @POST("create")
    Call<BaseModel<EmployeeResp>> createEmployee(@Body Employee employee );

    @PUT("update/{id}")
    Call<BaseModel<EmployeeResp>> updateEmployee(@Path("id") int id, @Body Employee employee );

    @DELETE("delete/{id}")
    Call<BaseModel<String>> deleteEmployee(@Path("id") int id);
}
