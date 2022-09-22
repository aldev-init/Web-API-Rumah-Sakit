package DTO.DaftarShift;

import java.time.LocalDateTime;

public class UpdateDaftarShiftRequest {
    public String kategori;
    public long foreign_id;
    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;
}
