package DTO.DaftarShift;

import java.time.LocalDateTime;

public class CreateDaftarShiftRequest {
    public String kategori;
    public long foreign_id;
    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;
}
