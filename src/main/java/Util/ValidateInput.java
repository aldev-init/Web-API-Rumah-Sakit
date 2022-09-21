package Util;

public class ValidateInput {

    public static String[] posisiStaff = {"Security","Janitor","Receipt","Engineer"};

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
}
