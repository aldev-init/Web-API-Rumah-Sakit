package DTO.JadwalPraktikDTO;

import java.time.LocalDateTime;

public class CreateJadwalPraktikRequest {
    public String hari;
    public LocalDateTime start_time;
    public LocalDateTime end_time;
    public String deskripsi;
    public long dokter_id;
}
