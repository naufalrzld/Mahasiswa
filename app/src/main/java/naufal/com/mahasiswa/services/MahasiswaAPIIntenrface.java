package naufal.com.mahasiswa.services;

import naufal.com.mahasiswa.MahasiswaResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MahasiswaAPIIntenrface {
    @FormUrlEncoded
    @POST("inputData.php")
    Call<MahasiswaResponse> tambahData(@Field("nim") String nim, @Field("nama") String nama,
                                       @Field("kelas") String kelas, @Field("jurusan") String jurusan);

    @FormUrlEncoded
    @POST("updateDataMahasiswa.php")
    Call<MahasiswaResponse> updateData(@Field("nim") String nim, @Field("nama") String nama,
                                       @Field("kelas") String kelas, @Field("jurusan") String jurusan);

    @FormUrlEncoded
    @POST("deleteDataMahasiswa.php")
    Call<MahasiswaResponse> deleteData(@Field("nim") String nim);

    @GET("getDataMahasiswa.php")
    Call<MahasiswaResponse> getDataMahasiswa();
}
