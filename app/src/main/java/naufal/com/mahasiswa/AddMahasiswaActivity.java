package naufal.com.mahasiswa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mahasiswa);

        etNIM = findViewById(R.id.et_nim);
        etNama = findViewById(R.id.et_nama);
        etKelas = findViewById(R.id.et_kelas);
        etJurusan = findViewById(R.id.et_jurusan);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    String nim = etNIM.getText().toString();
                    String nama = etNama.getText().toString();
                    String kelas = etKelas.getText().toString();
                    String jurusan = etJurusan.getText().toString();

                    addDataMahasiswa(nim, nama, kelas, jurusan);
                }
            }
        });
    }

    private boolean isInputValid() {
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
                        boolean error = response.body().getError();
                        if (!error) {
                            Toast.makeText(getApplicationContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            clearLayout();
                        }
                    }

                    @Override
                    public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
