package naufal.com.mahasiswa;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {
    private Context context;
    private List<MahasiswaModel> listMahasiswa;

    public MahasiswaAdapter(Context context) {
        this.context = context;
        listMahasiswa = new ArrayList<>();
    }

    public void setData(List<MahasiswaModel> listMahasiswa) {
        this.listMahasiswa = listMahasiswa;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MahasiswaModel mahasiswaModel = listMahasiswa.get(position);

        holder.tvNIM.setText(mahasiswaModel.getNim());
        holder.tvNama.setText(mahasiswaModel.getNama());
        holder.tvKelas.setText(mahasiswaModel.getKelas());
        holder.tvJurusan.setText(mahasiswaModel.getJurusan());
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mahasiswaModel.getNim(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cvItem;
        private TextView tvNIM, tvNama, tvKelas, tvJurusan;

        public ViewHolder(View itemView) {
            super(itemView);

            cvItem = itemView.findViewById(R.id.cv_item);
            tvNIM = itemView.findViewById(R.id.tv_nim);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvKelas = itemView.findViewById(R.id.tv_kelas);
            tvJurusan = itemView.findViewById(R.id.tv_jurusan);
        }
    }
}
