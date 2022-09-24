package Util;

public class ValidateInput {

    public static String[] posisiStaff = {"Security","Janitor","Receipt","Engineer"};
    public static String[] pegawaiKategori = {"dokter","perawat","staff"};

    public static String[] hari = {"senin","selasa","rabu","kamis","jumat","sabtu","minggu"};

    public static String[] kategoriObat = {"syrup","pil","tablet","cair","other"};

    public static String[] kategoriRuangan = {"standard","vip","vvip"};

    public static boolean EmailInput(String input){
        if(!input.contains("@")){
            return false;
        }
        return true;
    }

    public static boolean PhoneNumberInput(String input){
        if(!input.contains("+62")){
            return false;
        }

        return true;
    }

    public static boolean BooleanInput(int input){
        try{
            int testInp = input;
            if(testInp > 1 || testInp < 0){
                return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean BooleanInput(String input){
        try{
            int toInt = Integer.parseInt(input);
            if(toInt > 1 || toInt < 0){
                return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean UserTypeInput(String input){
        try{
            String testInp = input;
            if(!testInp.contains("Super Admin") || !testInp.contains("Admin")){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean positionStaffInput(String input){
        if(input.equals(posisiStaff[0]) || input.equals(posisiStaff[1]) || input.equals(posisiStaff[2]) || input.equals(posisiStaff[3])){
            return true;
        }
        return false;
    }

    public static boolean pegawaiKategori(String input){
        String lower = input.toLowerCase();
        if(lower.equals(pegawaiKategori[0]) || lower.equals(pegawaiKategori[1]) || lower.equals(pegawaiKategori[2])){
            return true;
        }
        return false;
    }

    public static boolean daftarShiftKategori(String input){
        String lower = input.toLowerCase();
        if(lower.equals(pegawaiKategori[1]) || lower.equals(pegawaiKategori[2])){
            return true;
        }

        return false;
    }

    public static boolean hariInput(String input){
        String lower = input.toLowerCase();
        if(lower.equals(hari[0]) || lower.equals(hari[1]) || lower.equals(hari[2])
        || lower.equals(hari[3]) || lower.equals(hari[4]) || lower.equals(hari[5])
        || lower.equals(hari[6])){
            return true;
        }

        return false;
    }

    public static boolean kategoriObat(String input){
        String lower = input.toLowerCase();
        if(lower.equals(kategoriObat[0]) || lower.equals(kategoriObat[1]) || lower.equals(kategoriObat[2])
        || lower.equals(kategoriObat[3]) || lower.equals(kategoriObat[4])){
            return true;
        }
        return false;
    }

    public static boolean kategoriRuangan(String input){
        String lower = input.toLowerCase();
        if(lower.equals(kategoriRuangan[0]) || lower.equals(kategoriRuangan[1]) || lower.equals(kategoriRuangan[2])){
            return true;
        }

        return false;
    }
}
