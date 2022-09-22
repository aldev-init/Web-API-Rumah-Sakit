package DTO.JadwalPraktikDTO;

import java.time.LocalDateTime;

public class UpdateJadwalPraktikRequest {
    public String hari;
    public LocalDateTime start_time;
    public LocalDateTime end_time;
    public String deskripsi;
    public long dokter_id;
}
