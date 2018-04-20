package org.ParkSys.service;

import org.ParkSys.bean.CarIn;
import org.ParkSys.dao.CarInDao;
import org.ParkSys.enums.CarType;
import org.ParkSys.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CarInService {

    @Autowired
    private CarInDao carInDao;

    private long amont = 200;

    //入库
    public boolean saveCar(String carNo, CarType carType) {
        boolean isSuccess = false;
        Date startTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CarIn car = new CarIn();
        List<CarIn> carIns = carInDao.findByCarNo(carNo);
        for (CarIn carIn : carIns) {
            if (carIn.getStatus().equals(Status.exist)) {
                return isSuccess;
            }
        }
        car.setCarNo(carNo);
        car.setType(carType);
        car.setStartDateStr(simpleDateFormat.format(startTime));
        if (carType.equals(CarType.small)) {
            car.setPrice(8.00);
            car.setCarType("小型车");
        } else if (carType.equals(CarType.middle)) {
            car.setPrice(15.00);
            car.setCarType("中型车");
        } else if (carType.equals(CarType.big)) {
            car.setPrice(30.00);
            car.setCarType("大型车");
        }
        car.setStartDate(startTime);
        car.setStatus(Status.exist);
        try {
            carInDao.save(car);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    //getAll
    public long getInCount() {
        long count = 0;
        try {
            count = carInDao.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    //    剩余车位数
    public long inCount() {
        long inCount = carInDao.count();
        return inCount;
    }
}
