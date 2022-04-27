package ru.mirea.panin.practice6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText, editText2;
    private TextView textView;
    private SharedPreferences preferences;
    final String SAVED_TEXT = "saved_text";
    final String SAVED_NAME = "saved_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        textView = findViewById(R.id.textView);
        preferences = getPreferences(MODE_PRIVATE);
    }

    public void onSaveText(View v) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVED_TEXT, editText.getText().toString());
        editor.putString(SAVED_NAME, editText2.getText().toString());
        editor.apply();

        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    public void onLoadText(View v) {
        String text = preferences.getString(SAVED_TEXT, "Empty") + " " + preferences.getString(SAVED_NAME, "Empty");
        textView.setText(text);
    }
}