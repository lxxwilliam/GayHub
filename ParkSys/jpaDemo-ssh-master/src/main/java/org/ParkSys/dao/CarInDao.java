package org.ParkSys.dao;

import org.ParkSys.bean.CarIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface CarInDao extends JpaRepository<CarIn, Serializable> {

    List<CarIn> findByCarNo(String carNo);

    @Query("select carIn from CarIn carIn where datediff(carIn.startDate,now()) = -:n")
    List<CarIn> countInTodayn(@Param("n") int n);

    @Query("select count(1) from CarIn carIn where carIn.status=1")
    long count();
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CarIn carIn set carIn.status=0 where carIn.id= :id")
    void updateStatus(@Param("id") long id);
}
