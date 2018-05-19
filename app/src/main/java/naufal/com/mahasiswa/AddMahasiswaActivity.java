package naufal.com.mahasiswa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import naufal.com.mahasiswa.services.RetrofitServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMahasiswaActivity extends AppCompatActivity {
    private EditText etNIM, etNama, etKelas, etJurusan;
    private Button btnSimpan;

    private String status, nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mahasiswa);

        etNIM = findViewById(R.id.et_nim);
        etNama = findViewById(R.id.et_nama);
        etKelas = findViewById(R.id.et_kelas);
        etJurusan = findViewById(R.id.et_jurusan);
        btnSimpan = findViewById(R.id.btn_simpan);

        /*
        * Baris 47 berfungsi untuk menerima intent dari activity lain
        * Baris 48 berfungsi untuk menerima data yang diberikan dari activity lain (jika ada)
        * "status" disana maksudnya adalah key data yang diberikan dari activity lain
        * "status" dalam kasus ini akan berisi nilai "add" atau "edit"
        * Untuk melihat isi dari key "status", bisa dilihat dari activity sebelumnya yang memanggal activity ini
        * Activity sebelumnya yang memanggil activitu ini yaitu MainActivity dan MahasiswaAdapter
        * Bisa dilihat pada MainActivity baris 61 key "status" berisikan nilai "add"
        * Dan pada MahasiswaAdapter baris 55 key "status" berisikan nilai "add"
        */
        Intent dataIntent = getIntent();
        status = dataIntent.getStringExtra("status");

        if (status.equals("edit")) {
            /*
            * Jika proses edit data, data nim, nama, kelas dan jurusan diambil dari list
            * List tersebut berada pada main activity
            * Untuk mengambil data tersebut bisa menggunakan method getStringExtra (jika data bertipekan String)
            * Jika data bertipekan integer maka gunakan getIntExtra begitu seterusnya
            * Kemudian data tersebut disimpan ke dalam variable
            */
            nim = dataIntent.getStringExtra("nim");
            String nama = dataIntent.getStringExtra("nama");
            String kelas = dataIntent.getStringExtra("kelas");
            String jurusan = dataIntent.getStringExtra("jurusan");

            /*
            * Untuk proses edit, NIM harus di disable karena NIM merupakan primary key
            * Primary key tidak boleh diubah datanya
            * Dengan mendisable edit text nim, nim tidak bisa dirubah
            * */
            etNIM.setEnabled(false);

            etNIM.setText(nim);
            etNama.setText(nama);
            etKelas.setText(kelas);
            etJurusan.setText(jurusan);
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * Cek apakah field sudah diisi semua atau belum
                * Jika sudah diisi semua maka ambil semua text yang ada pada setiap edit text kemudian disimpan ke dalam varable (baris 84- 87)
                */
                if (isInputValid()) {
                    nim = etNIM.getText().toString();
                    String nama = etNama.getText().toString();
                    String kelas = etKelas.getText().toString();
                    String jurusan = etJurusan.getText().toString();

                    /*
                    * Jika status berisikan nilai "edit" maka procedure yang dipanggil adalah procedure updateData
                    * Jika status berisikan niali lainnya (dalam kasus ini yaitu "add") maka procedure yang dipanggil adalah procedure addDataMahasiswa
                    * */
                    if (status.equals("edit")) {
                        updateData(nim, nama, kelas, jurusan);
                    } else {
                        addDataMahasiswa(nim, nama, kelas, jurusan);
                    }
                }
            }
        });
    }

    private boolean isInputValid() {
        /*
        * Function ini untuk mengecek apakah edit text NIM, nama kelas dan jurusan sudah diisi semua atau belum
        * Jika edit text belum diisi (semua atau salah satu) maka nilai baliknya akan bernilai FALSE
        * Jika edit text sudah diisi semua maka nilai baliknya akan bernilai true
        * Proses pengecekan terjadi mulai dari baris 112 - 131
        * Jika masih ada edit text yang kosong maka tampilkan pesan error pada edit text tersebut
        *
        */
        if (TextUtils.isEmpty(etNIM.getText().toString()) ||TextUtils.isEmpty(etNama.getText().toString())
                || TextUtils.isEmpty(etKelas.getText().toString()) || TextUtils.isEmpty(etJurusan.getText().toString())) {
            if (TextUtils.isEmpty(etNIM.getText().toString())) {
                etNIM.setError("NIM harus diisi!");
            }

            if (TextUtils.isEmpty(etNama.getText().toString())) {
                etNama.setError("Nama harus diisi!");
            }

            if (TextUtils.isEmpty(etKelas.getText().toString())) {
                etKelas.setError("Kelas harus diisi!");
            }

            if (TextUtils.isEmpty(etJurusan.getText().toString())) {
                etJurusan.setError("Jurusan harus diisi");
            }

            return false;
        }

        return true;
    }

    private void clearLayout() {
        /*
        * Ketika sudah mengkilik button simpan dan prosesnya sukses maka edit text dibersihkan
        */
        etNIM.setText("");
        etNama.setText("");
        etKelas.setText("");
        etJurusan.setText("");
    }

    private void addDataMahasiswa(String nim, String nama, String kelas, String jurusan) {
        try {
            Call<MahasiswaResponse> call = RetrofitServices.sendMahasiswaRequest().tambahData(nim, nama, kelas, jurusan);
            if (call != null) {
                call.enqueue(new Callback<MahasiswaResponse>() {
                    @Override
                    public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                        /*
                        * Jika server (inputData.php) merespon maka procedure ini akan dijalankan
                        * Baris 163 variable error diisi dengan status dari server yang bernilai TRUE atau FALSE
                        * Status error tersebut bisa dilihat pada file inputData.php untuk mengetahui kapan bernilai TRUE dan kapan bernilai FALSE
                        * Baris 164 variable msg diisi dengan message dari server
                        * Message tersebut berisi keterangan apakah data berhasil diinput atau tidak. Bisa dilihat pada file inputData.php
                        * Kemudian message tersebut ditampilkan kelayar dengan menggunakan TOAST (baris 165)
                        * Jika proses input data tidak terjadi error maka edit text dibersihkan (baris 167)
                        * Untuk mencoba agar proses error, coba inputkan NIM yang sama dengan yang sudah diinputkan sebelumnya
                        */
                        boolean error = response.body().getError();
                        String msg = response.body().getMessage();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        if (!error) {
                            clearLayout();
                        }
                    }

                    @Override
                    public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                        /*
                        * Jika server tidak merespon atau koneksi ke server tidak berjalan dengan baik, maka procedure ini akan dijalankan
                        * Kemudian errornya ditampilkan ke Logcat (baris 177)
                        */
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData(String nim, String nama, String kelas, String jurusan) {
        try {
            Call<MahasiswaResponse> call = RetrofitServices.sendMahasiswaRequest().updateData(nim, nama, kelas, jurusan);
            if (call != null) {
                call.enqueue(new Callback<MahasiswaResponse>() {
                    @Override
                    public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                        /*
                         * Jika server (updateDataMahasiswa.php) merespon maka procedure ini akan dijalankan
                         * Baris 204 variable error diisi dengan status dari server yang bernilai TRUE atau FALSE
                         * Status error tersebut bisa dilihat pada file updateDataMahasiswa.php untuk mengetahui kapan bernilai TRUE dan kapan bernilai FALSE
                         * Baris 205 variable msg diisi dengan message dari server
                         * Message tersebut berisi keterangan apakah data berhasil diupdate atau tidak. Bisa dilihat pada file updateDataMahasiswa.php
                         * Kemudian message tersebut ditampilkan kelayar dengan menggunakan TOAST (baris 206)
                         * Jika proses input data tidak terjadi error maka kembali ke MainActivity dengan cara memanggil procedure finish()(baris 208)
                         * Procedure finish() digunakan untuk memfinish suatu activity dan akan kembali ke activity sebelumnya jika ada, jika tidak akan menutup program
                         * Untuk mencoba agar proses error, coba inputkan NIM yang sama dengan yang sudah diinputkan sebelumnya
                         */
                        boolean error = response.body().getError();
                        String msg = response.body().getMessage();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        if (!error) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                        /*
                         * Jika server tidak merespon atau koneksi ke server tidak berjalan dengan baik, maka procedure ini akan dijalankan
                         * Kemudian errornya ditampilkan ke Logcat (baris 218)
                         */
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteData(String nim) {
        try {
            Call<MahasiswaResponse> call = RetrofitServices.sendMahasiswaRequest().deleteData(nim);
            if (call != null) {
                call.enqueue(new Callback<MahasiswaResponse>() {
                    @Override
                    public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                        /*
                         * Jika server (deleteDataMahasiswa.php) merespon maka procedure ini akan dijalankan
                         * Baris 245 variable error diisi dengan status dari server yang bernilai TRUE atau FALSE
                         * Status error tersebut bisa dilihat pada file deleteDataMahasiswa.php untuk mengetahui kapan bernilai TRUE dan kapan bernilai FALSE
                         * Baris 246 variable msg diisi dengan message dari server
                         * Message tersebut berisi keterangan apakah data berhasil didelete atau tidak. Bisa dilihat pada file deleteDataMahasiswa.php
                         * Kemudian message tersebut ditampilkan kelayar dengan menggunakan TOAST (baris 247)
                         * Jika proses input data tidak terjadi error maka kembali ke MainActivity dengan cara memanggil procedure finish()(baris 249)
                         * Procedure finish() digunakan untuk memfinish suatu activity dan akan kembali ke activity sebelumnya jika ada, jika tidak akan menutup program
                         */
                        boolean error = response.body().getError();
                        String msg = response.body().getMessage();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        if (!error) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                        /*
                         * Jika server tidak merespon atau koneksi ke server tidak berjalan dengan baik, maka procedure ini akan dijalankan
                         * Kemudian errornya ditampilkan ke Logcat (baris 258)
                         */
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_mahasiswa_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem delete = menu.findItem(R.id.delete);

        if (status.equals("add")) {
            delete.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteData(nim);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
