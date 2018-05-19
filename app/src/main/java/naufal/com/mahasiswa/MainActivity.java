package naufal.com.mahasiswa;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import naufal.com.mahasiswa.services.RetrofitServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvList;
    private FloatingActionButton fabAdd;

    private MahasiswaAdapter adapter;

    private List<MahasiswaModel> listMahasiswa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe);
        rvList = findViewById(R.id.rv_list);
        fabAdd = findViewById(R.id.fab_add);

        adapter = new MahasiswaAdapter(this);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataMahasiswa();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddMahasiswaActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataMahasiswa();
    }

    private void getDataMahasiswa() {
        swipeRefreshLayout.setRefreshing(true);
        listMahasiswa.clear();

        try {
            Call<MahasiswaResponse> call = RetrofitServices.sendMahasiswaRequest().getDataMahasiswa();
            if (call != null) {
                call.enqueue(new Callback<MahasiswaResponse>() {
                    @Override
                    public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        listMahasiswa = response.body().getMahasiswa();

                        adapter.setData(listMahasiswa);
                    }

                    @Override
                    public void onFailure(Call<MahasiswaResponse> call, Throwable t) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
