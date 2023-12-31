package com.example.plantsearching;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignInActivity extends AppCompatActivity {

    private String userName, Password ;
    // creating variable for button
    private Button signupBtn;
    private EditText editUsername, editPassword;
    Button buttonlogin, back_signin;
    TextView Register;
    CheckBox checkboxRememberMe;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        // Khởi tạo SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //sharedPreferences  = getSharedPreferences("users", Context.MODE_PRIVATE);
        editUsername = findViewById(R.id.Get_Gmail);
        editPassword = findViewById(R.id.get_Password);
        buttonlogin = findViewById(R.id.btn_SignIn);
        Register = findViewById(R.id.Register);
        checkboxRememberMe = findViewById(R.id.checkbox_remember_me);
        //back_signin.findViewById(R.id.back_321);


       /* back_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Introduce2Activity.class);
                startActivity(intent);
                finish();
            }
        });*/
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting data from edittext fields.
                String userName = editUsername.getText().toString();
                String Password = editPassword.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(userName)) {
                    editUsername.setError("Please enter User Name");
                }
                if(Validate.ValidateGmail(userName) == false){
                    Toast.makeText(getApplicationContext(),"Username phải trên 6 ký tự",Toast.LENGTH_LONG).show();
                    return;
                }
                if(Validate.ValidatePassword(Password) == false){
                    Toast.makeText(getApplicationContext(),"Password phải trên 6 ký tự",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    editPassword.setError("Please enter password");
                }
                db.collection("users")
                        .whereEqualTo("email",userName)
                        .whereEqualTo("password", Encrypt.HashPasswordMd5(Password))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(!task.getResult().isEmpty()){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", userName);
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                                    // luu trang thai
                                    if (checkboxRememberMe.isChecked()) {
                                        // Xử lý khi CheckBox được chọn
                                        // Ví dụ: lưu trạng thái đăng nhập của người dùng
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.apply();
                                    }

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Đăng nhập thất bại",Toast.LENGTH_LONG).show();

                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Đăng nhập thất bại",Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
    }
}




