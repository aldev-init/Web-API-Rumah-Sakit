package Util;

public class ValidateInput {
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
}
