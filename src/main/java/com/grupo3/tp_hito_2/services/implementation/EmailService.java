package com.grupo3.tp_hito_2.services.implementation;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service 
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remitente;

    public EmailService(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }

    public void enviarCredenciales(String emailDestino,String nombre,String password,boolean reactivacion){
        try{
            SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(emailDestino);
       
        if(reactivacion){
            mensaje.setSubject("Reactivacion exitosa");
            mensaje.setText("Hola," + nombre + "\n\n Tu reactivacion fue exitosa, para iniciar sesion usa las siguientes " +
        "credenciales, \n usuario: " + emailDestino +
        "\n contraseña: "+password);
        }else{
            mensaje.setSubject("Registro exitoso");
            mensaje.setText("Hola," + nombre + "\n\n Tu registro fue exitoso, para iniciar sesion usa las siguientes " +
        "credenciales, \n usuario: " + emailDestino +
        "\n contraseña: "+password);
        }
        
        mailSender.send(mensaje);
        }catch(MailException e){
            throw new IllegalStateException("No se pudo enviar el mail con las credenciales. ",e);
        }
    }

}
