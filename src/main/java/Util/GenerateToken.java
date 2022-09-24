package Util;

import Models.UserPremission;
import Models.Users;
import io.smallrye.jwt.build.Jwt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class GenerateToken {
    public static String getToken(Users users){
        List<String> userPremission = new ArrayList<>();
        List<UserPremission> premission = UserPremission.find("user_id = ?1",users.getId()).list();
        for(int i = 0;i<premission.size();i++){
            userPremission.add(premission.get(i).getName());
        }
        String token = Jwt.issuer("aldev-resource")
                .upn(users.getName())
                .groups(new HashSet<>(userPremission))
                .expiresIn(18000)
                .sign();
        return  token;

    }
}
