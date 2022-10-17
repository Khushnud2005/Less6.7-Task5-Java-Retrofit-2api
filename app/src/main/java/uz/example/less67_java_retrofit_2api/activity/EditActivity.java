package uz.example.less67_java_retrofit_2api.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import uz.example.less67_java_retrofit_2api.MainActivity;
import uz.example.less67_java_retrofit_2api.R;

public class EditActivity extends AppCompatActivity {
    EditText et_name;
    EditText et_salary;
    EditText et_age;
    Button btn_edit;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
    }

    void initViews(){
        Bundle extras = getIntent().getExtras();
        et_name = findViewById(R.id.et_nameEdit);
        et_salary = findViewById(R.id.et_salaryEdit);
        et_age = findViewById(R.id.et_ageEdit);
        btn_edit = findViewById(R.id.btn_submitEdit);
        if (extras !=null){
            Log.d("###","extras not NULL - ");
            et_name.setText(extras.getString("name"));
            et_salary.setText(extras.getString("salary"));
            et_age.setText(extras.getString("age"));
            id = extras.getString("id");
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String salary = et_salary.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("salary",salary);
                intent.putExtra("age",age);
                intent.putExtra("id",id);
                //setResult(88,intent);
                startActivity(intent);
            }
        });

    }
}