package Util;

import Models.Users;
import io.smallrye.jwt.build.Jwt;

public class GenerateToken {
    public static String getToken(Users users){
        String token = Jwt.issuer("aldev-resource")
                .upn(users.getName())
                .groups(users.getUserType())
                .sign();
        return  token;
    }
}
