package com.grupo3.tp_hito_2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.grupo3.tp_hito_2.entities.User;
import java.util.Optional;
import java.io.Serializable;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, Serializable> {

    @Query("SELECT u FROM User u JOIN FETCH u.userRoles WHERE u.username = (:username)")
	public abstract User findByUsernameAndFetchUserRolesEagerly(@Param("username") String username);

    Optional<User> findByUsername(String username);
}
