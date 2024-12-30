package com.example.sqlitelibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG_TAG = "log";
    Button btnAdd, btnClear;
    EditText etBookNam, etAuthor;
    AppDatabase appDatabase;
    BookDao bookDao;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    List<Book> bookList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = new ArrayList<>();
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etBookNam = findViewById(R.id.etBookName);
        etAuthor = findViewById(R.id.etAuthor);

        appDatabase = AppDatabase.getInstance(this);
        bookDao = appDatabase.bookDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            bookList.addAll(bookDao.getAllBooks());
            Log.d(LOG_TAG, "Got all book");
        });

        myAdapter = new MyAdapter(bookList);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {

        String bookName = etBookNam.getText().toString();
        String author = etAuthor.getText().toString();

        switch (v.getId()) {

            case R.id.btnAdd:
                Book newBook = new Book(bookName, author);
                addedInRecycler(newBook);
                Executors.newSingleThreadExecutor().execute(() -> {
                    bookDao.insert(newBook);
                    Log.d(LOG_TAG, "Book inserted " + bookName + ", " + author);
                });

                break;
            case R.id.btnClear:
                clearInRecycler();
                Executors.newSingleThreadExecutor().execute(() -> {
                    bookDao.clearTable();
                    Log.d(LOG_TAG, "Table cleared");
                });
                break;
        }
    }

    private void clearInRecycler() {
        myAdapter.deleteData();
        recyclerView.setAdapter(myAdapter);
    }

    private void addedInRecycler(Book newBook) {
        myAdapter.addData(newBook);
        recyclerView.setAdapter(myAdapter);
    }
}