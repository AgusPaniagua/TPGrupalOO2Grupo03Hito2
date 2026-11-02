package com.grupo3.tp_hito_2.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component 
public class GeneradorPassword {
    private static final String CARACTERES = "abcdefghjkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789@-_";
    private static  final int LARGO=10;
    private final SecureRandom random = new SecureRandom();
    
    public String generarPasword(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<LARGO;i++){
            int posicion = random.nextInt(CARACTERES.length());
            sb.append(CARACTERES.charAt(posicion));
        }
        return sb.toString();
    }

    
}
