package uz.example.less67_java_retrofit_2api;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.example.less67_java_retrofit_2api.activity.CreateActivity;
import uz.example.less67_java_retrofit_2api.adapter.EmployeeAdapter;
import uz.example.less67_java_retrofit_2api.model.BaseModel;
import uz.example.less67_java_retrofit_2api.model.Employee;
import uz.example.less67_java_retrofit_2api.model.EmployeeResp;
import uz.example.less67_java_retrofit_2api.network.RetrofitHttp;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Employee> employees = new ArrayList<>();
    ProgressBar pb_loading;
    FloatingActionButton floating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    void initViews(){
        pb_loading = findViewById(R.id.pb_loading);
        floating = findViewById(R.id.floating);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        apiEmployeeList();
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateActivity();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            Log.d("###","extras not NULL - ");
            String edit_name = extras.getString("name");
            String edit_salary = extras.getString("salary");
            String edit_age = extras.getString("age");
            String edit_id = extras.getString("id");
            Employee employee = new Employee(Integer.parseInt(edit_id),edit_name,Integer.parseInt(edit_salary),Integer.parseInt(edit_age));
            Toast.makeText(MainActivity.this, "Employee Prepared to Edit", Toast.LENGTH_LONG).show();

            apiEmployeeUpdate(employee);

        }
    }
    public ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == 78) {
                        Intent data = result.getData();

                        if (data !=null){
                            String new_name = data.getStringExtra("name");
                            String new_salary = data.getStringExtra("salary");
                            String new_age = data.getStringExtra("age");
                            Employee employee = new Employee(new_name,Integer.parseInt(new_salary),Integer.parseInt(new_age));
                            Toast.makeText(MainActivity.this, "Title modified", Toast.LENGTH_LONG).show();

                            apiEmployeeCreate(employee);
                        }
                        // your operation....
                    }else {
                        Toast.makeText(MainActivity.this, "Operation canceled", Toast.LENGTH_LONG).show();
                    }

                }
            });
    void openCreateActivity(){
        Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        launchSomeActivity.launch(intent);
    }
    void refreshAdapter(ArrayList<Employee> employees) {
        EmployeeAdapter adapter = new EmployeeAdapter(this, employees);
        recyclerView.setAdapter(adapter);
    }
    public void dialogEmployee(Employee employee) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this poster?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        apiEmployeeDelete(employee);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void apiEmployeeList() {
        pb_loading.setVisibility(View.VISIBLE);
        RetrofitHttp.employeeService.listEmployee().enqueue(new Callback<BaseModel<ArrayList<EmployeeResp>>>() {
            @Override
            public void onResponse(Call<BaseModel<ArrayList<EmployeeResp>>> call, Response<BaseModel<ArrayList<EmployeeResp>>> response) {
                pb_loading.setVisibility(View.GONE);
                //Log.d("@@@", response.body().toString());
                Log.d("@@@", String.valueOf(response.code()));
                if (response.code() < 400){
                    employees.clear();
                    ArrayList<EmployeeResp> items = response.body().getData();
                    if (items!=null){
                        for (EmployeeResp item : items){
                            Employee employee = new Employee(item.getId(),item.getEmployee_name(),item.getEmployee_salary(),item.getEmployee_age());
                            employees.add(employee);
                        }
                    }
                    refreshAdapter(employees);
                }else {
                    Toast.makeText(MainActivity.this, "Status "+String.valueOf(response.code())+". Please wait Trying Again!!!", Toast.LENGTH_LONG).show();
                    apiEmployeeList();
                }

            }
            @Override
            public void onFailure(Call<BaseModel<ArrayList<EmployeeResp>>> call , Throwable t ) {
                Log.e("@@@", t.getMessage());
            }
        });
    }
    private void apiEmployeeCreate(Employee employee) {
        pb_loading.setVisibility(View.VISIBLE);
        RetrofitHttp.employeeService.createEmployee(employee).enqueue(new Callback<BaseModel<EmployeeResp>>() {
            @Override
            public void onResponse(Call<BaseModel<EmployeeResp>> call, Response<BaseModel<EmployeeResp>> response) {
                pb_loading.setVisibility(View.GONE);
                Log.d("@@@", String.valueOf(response.code()));
                if (response.body() != null && response.code() < 400){
                    Log.d("@@@onCreated", response.body().getData().toString());
                    Toast.makeText(MainActivity.this,"Employer "+ employee.getEmployee_name() +" Created", Toast.LENGTH_LONG).show();
                    apiEmployeeList();
                }else {
                    Toast.makeText(MainActivity.this, "Status "+String.valueOf(response.code())+". Please wait Trying Again!!!", Toast.LENGTH_LONG).show();
                    apiEmployeeCreate(employee);
                }


            }
            @Override
            public void onFailure(Call<BaseModel<EmployeeResp>> call , Throwable t ) {
                Log.e("@@@", t.getMessage().toString());
                //Log.d("@@@", String.valueOf(response.code()));
                apiEmployeeCreate(employee);
            }
        });
    }
    private void apiEmployeeUpdate(Employee employee) {
        pb_loading.setVisibility(View.VISIBLE);
        RetrofitHttp.employeeService.updateEmployee(employee.getId(),employee).enqueue(new Callback<BaseModel<EmployeeResp>>() {
            @Override
            public void onResponse(Call<BaseModel<EmployeeResp>> call, Response<BaseModel<EmployeeResp>> response) {
                pb_loading.setVisibility(View.GONE);
                if (response.code() < 400) {
                    Log.d("@@@onEdit", response.body().getData().toString());
                    Toast.makeText(MainActivity.this,"Employer "+  employee.getEmployee_name() + " Updated", Toast.LENGTH_LONG).show();
                    apiEmployeeList();

                }else {
                    apiEmployeeUpdate(employee);
                    Toast.makeText(MainActivity.this, "Status "+String.valueOf(response.code())+". Please wait Trying Again!!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BaseModel<EmployeeResp>> call , Throwable t ) {
                Log.e("@@@", t.getMessage().toString());
                apiEmployeeUpdate(employee);
            }
        });
    }
    private void apiEmployeeDelete(Employee employee) {
        pb_loading.setVisibility(View.VISIBLE);
        RetrofitHttp.employeeService.deleteEmployee(employee.getId()).enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                pb_loading.setVisibility(View.GONE);
                if (response.body() != null && response.code() < 400) {
                    Log.d("@@@onDelete", response.body().getData());
                    Toast.makeText(MainActivity.this, response.body().getData() + "-Employer Deleted", Toast.LENGTH_LONG).show();
                    apiEmployeeList();

                }else{
                    apiEmployeeDelete(employee);
                    Toast.makeText(MainActivity.this, "Status "+String.valueOf(response.code())+". Please wait Trying Again!!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BaseModel<String>> call , Throwable t ) {
                Log.e("@@@", t.getMessage().toString());
                apiEmployeeDelete(employee);
            }
        });
    }
}