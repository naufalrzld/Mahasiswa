package naufal.com.mahasiswa;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
                /*
                * Ketia fab_add di tekan maka membuka activity baru dengan membawa data status yang berisi add
                * Data ini berfungsi untuk mendefinisikan apakah activity AddMahasiswaActivity digunakan untuk menambahkan data atau mengedit data
                * Dalam kasus ini, activity AddMahasiswaActivity digunakan untuk menambahkan data
                */
                Intent i = new Intent(MainActivity.this, AddMahasiswaActivity.class);
                i.putExtra("status", "add");
                startActivity(i);
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
                        /*
                        * Ketika server (getDataMahasiswa.php) merespon, maka procedure ini dijalankan dan swiperefreshnya diset FALSE (baris 96)
                        * Proses loadingnya akan hilang
                        * Baris 98 untuk mengambil status dari server (getDataMahasiswa.php) error atau tidak.
                        * Kemudian disimpan ke varaible error. Variable error nantinya akan bernilai TRUE atau FALSE
                        * Untuk melihat status kapan dia error dan kapan tidak error, bisa dilihat pada file getDataMahasiswa.php
                        * Baris 100 - 108 melakukan proses pengecekan variable error.
                        * Jika tidak error (!error artinya tidak error) maka variable listMahasiswa diisi dengan list mahasiswa dari database (baris 101)
                        * Kemudian isi dari variable listMahasiswa di set kedalam adapter yang nantinya akan ditampilkan ke layar (baris 103)
                        * Jika terjadi error maka message error dari server diambil kemudian disimpan ke valriable msg (baris 105)
                        * Untuk melihat message errornya bisa dilihat pada file getDataMahasiswa.php pada baris ke 21
                        * Kemudian isi variable msg ditampilkan dengan menggunakan TOAST (baris 107)
                        */
                        swipeRefreshLayout.setRefreshing(false);

                        boolean error = response.body().getError();

                        if (!error) {
                            listMahasiswa = response.body().getMahasiswa();

                            adapter.setData(listMahasiswa);
                        } else {
                            String msg = response.body().getMessage();

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                        /*
                        * Ketika server tidak merespon atau tidak terhubung ke server maka procedure ini dijalankan
                        * Swiperefreshnya diset FALSE (baris 118)
                        * Kemudian tampilkan errornya ke dalam Logcat (baris 119)
                        */
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
