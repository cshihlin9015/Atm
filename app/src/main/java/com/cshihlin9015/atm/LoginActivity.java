package com.cshihlin9015.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edUserid;
    private EditText edPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSharedPreferences("atm", MODE_PRIVATE)
                .edit()
                .putInt("LEVEL", 3)
                .putString("NAME", "Willy")
                .commit();

        int level = getSharedPreferences("atm", MODE_PRIVATE)
                .getInt("LEVEL", 0);
        Log.d(TAG, "onCreate: " + level);

        edUserid = findViewById(R.id.userid);
        edPasswd = findViewById(R.id.passwd);

        String userid = getSharedPreferences("atm", MODE_PRIVATE)
                .getString("USERID", "");
        edUserid.setText(userid);
    }

    public void login(View view) {
        final String userid = edUserid.getText().toString();
        final String passwd = edPasswd.getText().toString();

        /*if ("Willy".equals(userid) && "1234".equals(passwd)) {
            setResult(RESULT_OK);
            finish();
        }*/

        FirebaseDatabase.getInstance().getReference("users")
                .child(userid).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 資料來時會自動執行

                        String pw = (String) dataSnapshot.getValue();
                        if (pw.equals(passwd)) {
                            //save userid
                            getSharedPreferences("atm", MODE_PRIVATE)
                                    .edit()
                                    .putString("USERID", userid)
                                    .apply();

                            setResult(RESULT_OK);
                            finish();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登入結果")
                                    .setMessage("登入失敗")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void quit(View view) {

    }
}
