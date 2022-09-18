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
}
