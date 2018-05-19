package naufal.com.mahasiswa.services;

public class RetrofitServices {
    public static MahasiswaAPIIntenrface sendMahasiswaRequest() {
        return APIClient.getAPIClient().create(MahasiswaAPIIntenrface.class);
    }
}
