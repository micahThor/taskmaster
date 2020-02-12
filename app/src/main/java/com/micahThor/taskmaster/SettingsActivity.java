package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        // wire up save user name button
        Button saveUserName = findViewById(R.id.saveUserNameButton);
        saveUserName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get text in username text box
                EditText userNameTextEdit = findViewById(R.id.userNameTextView);
                String userName = userNameTextEdit.getText().toString();

                // save username if length is greater than 0.  Show message otherwise
                if (userName.length() > 0) {
                    SharedPreferences sharedPrefers = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPrefers.edit();
                    editor.putString("userName", userName);
                    editor.commit();
                    Toast toast = Toast.makeText(getApplicationContext(), "Successfully saved user name: " + userName, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "User name input required.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
