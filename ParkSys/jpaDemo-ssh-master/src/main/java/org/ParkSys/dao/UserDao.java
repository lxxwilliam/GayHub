package org.ParkSys.dao;

import org.ParkSys.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public interface UserDao extends JpaRepository<User, Serializable> {
    User findByUserName(String userName);

    long countByUserName(String userName);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User user set user.passWd = :pwd where user.id = :id")
    void updatePwd(@Param("id") long id,@Param("pwd") String pwd);
}
