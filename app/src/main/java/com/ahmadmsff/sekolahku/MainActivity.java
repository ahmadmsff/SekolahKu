package com.ahmadmsff.sekolahku;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ahmadmsff.sekolahku.APIHelper.APIServices;
import com.ahmadmsff.sekolahku.APIHelper.APIUtils;
import com.ahmadmsff.sekolahku.Adapter.StudentAdapter;
import com.ahmadmsff.sekolahku.Model.ListStudent;
import com.ahmadmsff.sekolahku.Model.Student;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<Student> students;
    private StudentAdapter studentAdapter;
    private APIServices apiServices;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout progressBar;
    private SwipeRefreshLayout swipeToRefresh;
    private Context context;
    private FloatingActionButton floatingActionButton;

    private EditText textSearch;

    boolean doubleBackToExitPressedOnce = false;

    private final int PAGE_SIZE = 10;
    private int pageIndex = 1;

    private boolean isRefreshing = false;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        init();
    }

    private void init() {
        students = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (RelativeLayout) findViewById(R.id.progressBar);
        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        textSearch = (EditText) findViewById(R.id.text_search);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        studentAdapter = new StudentAdapter(context, students);
        recyclerView.setAdapter(studentAdapter);

        textSearch.addTextChangedListener(textWatcher);

        swipeToRefresh.setOnRefreshListener(this);

        swipeToRefresh.post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddActivity.class);
                startActivity(intent);

            }
        });

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if(!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                        getNextData();
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        isLastPage = false;
        isRefreshing = true;
        getData();
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        apiServices = APIUtils.getAPIServices();

        apiServices.getSiswa().enqueue(new Callback<ListStudent>() {
            @Override
            public void onResponse(Call<ListStudent> call, Response<ListStudent> response) {
                progressBar.setVisibility(View.GONE);
                Boolean error = response.body().isError();
                if (!error) {
                    students.clear();
                    students.addAll(response.body().getStudent());
                    studentAdapter.notifyDataSetChanged();
                    swipeToRefresh.setRefreshing(false);
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListStudent> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Oops!, Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNextData() {

        pageIndex++;
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        apiServices = APIUtils.getAPIServices();
        apiServices.getNextSiswa(pageIndex).enqueue(new Callback<ListStudent>() {
            @Override
            public void onResponse(Call<ListStudent> call, Response<ListStudent> response) {
                if (response.isSuccessful()) {
                    Boolean error = response.body().isError();

                    if (!error) {
                        students.addAll(response.body().getStudent());
                        studentAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        swipeToRefresh.setRefreshing(false);
                        isLoading = false;
                        if (pageIndex == response.body().getPages()) {
                            isLastPage = true;
                        } else {
                            isLastPage = false;
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListStudent> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Oops!, Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchData() {
        progressBar.setVisibility(View.VISIBLE);
        String key = textSearch.getText().toString();

        if (key.equals("") || key == null) {
            getData();
        } else {
            apiServices = APIUtils.getAPIServices();

            apiServices.searchSiswa(key).enqueue(new Callback<ListStudent>() {
                @Override
                public void onResponse(Call<ListStudent> call, Response<ListStudent> response) {
                    progressBar.setVisibility(View.GONE);
                    Boolean error = response.body().isError();
                    if (!error) {
                        students = response.body().getStudent();
                        studentAdapter = new StudentAdapter(context, students);
                        recyclerView.setAdapter(studentAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ListStudent> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Oops!, Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() >= 3) {
                searchData();
            } else if (s.length() == 0) {
                getData();
            }

        }
    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
