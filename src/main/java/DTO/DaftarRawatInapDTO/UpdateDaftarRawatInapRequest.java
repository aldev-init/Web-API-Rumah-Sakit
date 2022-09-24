package DTO.DaftarRawatInapDTO;

import java.time.LocalDateTime;

public class UpdateDaftarRawatInapRequest {
    public long ruang_inap_id;
    public long dokter_id;
    public long pasien_id;
    public long perawat_satu_id;
    public long perawat_dua_id;
    public LocalDateTime start_date_time;
    public LocalDateTime end_date_time;
}
