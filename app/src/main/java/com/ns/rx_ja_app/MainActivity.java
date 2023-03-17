package com.ns.rx_ja_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ns.rx_ja_app.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    protected String TAG="MainActivity";
protected ActivityMainBinding binding;
protected FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> {
       if (binding.etMob.getText().toString().isEmpty()){
           binding.etMob.setError("Enter Mobile Number");
           binding.etMob.requestFocus();
       }else {
           PhoneAuthOptions options =
                   PhoneAuthOptions.newBuilder(mAuth)
                           .setPhoneNumber("+91"+binding.etMob.getText().toString().trim())       // Phone number to verify
                           .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                           .setActivity(this)                 // Activity (for callback binding)
                           .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                               @Override
                               public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                   Log.e(TAG, "onVerificationCompleted: ."+phoneAuthCredential.getSmsCode());
                               }

                               @Override
                               public void onVerificationFailed(@NonNull FirebaseException e) {
                                   Log.e(TAG, "onVerificationFailed: ."+e.getMessage());
                               }

                               @Override
                               public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                   super.onCodeSent(s, forceResendingToken);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("mob",binding.etMob.getText().toString().trim());
                                    bundle.putString("verificationId",s.toString());
                                    Intent intent=new Intent(MainActivity.this,OtpActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                               }
                           })
                           .build();
           PhoneAuthProvider.verifyPhoneNumber(options);

       }
        });

    }
}