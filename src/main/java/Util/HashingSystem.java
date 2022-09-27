package Util;

/*
    Algoritma Ini Milik Muhammad Alghifari
    Setelah berpusing ria ygy
    27-09-2022
 */


import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class HashingSystem {

    //secretkey
    @ConfigProperty(name = "HASHING_STEP")
    int step;

    public String Encrypt(String text){



        int index;

        List<Character> result = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        char[] chipertext = new char[56];
        //secret key 3 langkah
        char[] plaintext = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N',
                'O','P','Q','R','S','T','U'
                ,'V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0','!','@','#','$','%','^','&','*',
                '(',')','_','=','+','{','}',']','[',';',',','.'};
        //penolong index jika lebih besar dari plaintext.size
        int counter = 0;
        for(int ct = 0;ct<plaintext.length;ct++){
            //geser sebanyak n huruf
            index = ct + step;
            //jika index lebih besar dari plaintext.size maka kembali ke 0
            if(index >= plaintext.length){
                index = 0;
                //jika index kembali 0,tambahkan step yaitu counter
                if(index == 0){
                    index+=counter;
                }
                //agar bernilai sama dengan step,tambahkan counter saat index lebih besar dari plaintext.size
                counter++;
            }
            chipertext[ct] = plaintext[index];
        }

        //manual ygy
        /*char[] chipertext = {'D','E','F','G','H','I','J','K','L','M','N','O','P',
                'Q','R','S','T','U','V','W',
                'X','Y','Z','A','B','C'};*/

        String upper = text.toUpperCase();
        char[] upperchar = upper.toCharArray();

        //get index from input text and plaintext
        for(int i = 0;i<upperchar.length;i++){
            for(int j = 0;j<plaintext.length;j++){
                if(upperchar[i] == plaintext[j]){
                    temp.add(j);
                }
            }
        }

        //convert shift chipper
        for(int k = 0;k<temp.size();k++){
            result.add(chipertext[temp.get(k)]);
        }

        String chiperResult = "";
        //convert to string
        for(int s = 0;s<result.size();s++){
            chiperResult += result.get(s);
        }

        return chiperResult;
    }

    public boolean Verify(String text,String chiperText){
        if(text == chiperText){
            return true;
        }

        return false;
    }

}
