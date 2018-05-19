package naufal.com.mahasiswa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MahasiswaResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("msg")
    @Expose
    private String message;
    @SerializedName("mahasiswa")
    @Expose
    private List<MahasiswaModel> mahasiswa = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MahasiswaModel> getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(List<MahasiswaModel> mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

}
