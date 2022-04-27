package ru.mirea.panin.notebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class Editor extends Activity {
    private String LOG_TAG = Editor.class.getSimpleName();
    private EditText name, textNote;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);
        name = findViewById(R.id.editNameText);
        textNote = findViewById(R.id.editNoteText);
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);
        if (id != -1) {
            name.setText(MainActivity.notes.get(id));
            textNote.setText(getTextFromFile(MainActivity.notes.get(id)));
        } else {
            MainActivity.notes.add("");
            id = MainActivity.notes.size() - 1;
        }
    }

    public String getTextFromFile(String fileName) {
        FileInputStream fin = null;
        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(LOG_TAG, text);
            return text;
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        String fileName;
        String text = name.getText().toString();
        if (text.equals("")) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            fileName = "note_" + timeStamp + ".txt";
        } else
            fileName = name.getText().toString();
        System.out.println(fileName);
        try (FileOutputStream writer = openFileOutput(fileName, Context.MODE_PRIVATE)) {
            writer.write(textNote.getText().toString().getBytes());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        MainActivity.notes.set(id, fileName);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("notesList", Context.MODE_PRIVATE);
        HashSet<String> hashSet = new HashSet<>(MainActivity.notes);
        sharedPreferences.edit().putStringSet("notes", hashSet).apply();
    }
}
