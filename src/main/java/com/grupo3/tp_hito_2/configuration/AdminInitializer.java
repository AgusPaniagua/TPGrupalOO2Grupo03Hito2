package com.grupo3.tp_hito_2.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.grupo3.tp_hito_2.entities.Rol;
import com.grupo3.tp_hito_2.entities.User;
import com.grupo3.tp_hito_2.entities.UserRole;
import com.grupo3.tp_hito_2.repositories.IUserRepository;
import com.grupo3.tp_hito_2.repositories.IUserRoleRepository;

@Component 
public class AdminInitializer implements CommandLineRunner{

    private final IUserRepository userRepository;
    private final IUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public AdminInitializer(IUserRepository userRepository, IUserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder,
                            @Value("${ADMIN_USER:admin}") String adminUsername,
                            @Value("${ADMIN_PASSWORD:}") String adminPassword
    ){
        this.userRepository=userRepository;
        this.userRoleRepository=userRoleRepository;
        this.passwordEncoder=passwordEncoder;
        this.adminUsername=adminUsername;
        this.adminPassword=adminPassword;
    }

    @Override
    public void run(String... args){
        if(userRepository.findByUsername(adminUsername).isPresent()){
            return;
        }
        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                "Falta la variable de entorno ADMIN_PASSWORD para crear el administrador");
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        userRepository.save(admin);

        UserRole userRole = new UserRole();
        userRole.setUser(admin);
        userRole.setRole(Rol.ADMIN.name());
        userRoleRepository.save(userRole);
    }

}
