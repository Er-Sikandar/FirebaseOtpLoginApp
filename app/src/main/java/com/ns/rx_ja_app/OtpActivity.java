package com.ns.rx_ja_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ns.rx_ja_app.databinding.ActivityOtpBinding;

public class OtpActivity extends AppCompatActivity {
private String TAG="OtpActivity";
private ActivityOtpBinding binding;
private String mob="",verificationId="";
private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();

        Bundle bundle=getIntent().getExtras();
        mob=bundle.getString("mob","");
        verificationId=bundle.getString("verificationId","");
        Log.e(TAG, "onCreate: "+mob);

        binding.btnVerify.setOnClickListener(v -> {
         if (binding.etOtp.getText().toString().trim().isEmpty()){
             binding.etOtp.setError("Enter Mobile Number");
             binding.etOtp.requestFocus();
         }else {
             PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, binding.etOtp.getText().toString().trim());
             firebaseAuth.signInWithCredential(credential)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 startActivity(new Intent(OtpActivity.this,HomeActivity.class));
                                 finish();
                             } else {
                                 Toast.makeText(OtpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                             }
                         }
                     });

         }
        });

    }
}