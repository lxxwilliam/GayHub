package org.ParkSys.dao;

import org.ParkSys.bean.CarOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface CarOutDao extends JpaRepository<CarOut, Serializable> {

    CarOut findByCarNo(String carNo);

    List<CarOut> findCarOutsByCarNo(String carNo);

    long countByCarNo(String carNo);

    @Query("select carOut from CarOut carOut where datediff(carOut.endDate,now()) = -:n")
    List<CarOut> countOutTodayn(@Param("n")int n);
}
