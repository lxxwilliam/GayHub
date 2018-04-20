package org.ParkSys.service;

import org.ParkSys.bean.CarIn;
import org.ParkSys.bean.CarOut;
import org.ParkSys.dao.CarInDao;
import org.ParkSys.dao.CarOutDao;
import org.ParkSys.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CarOutService {

    @Autowired
    private CarOutDao carOutDao;
    @Autowired
    private CarInDao carInDao;

    //出库
    public CarOut getCar(String carNo) {
        List<CarIn> carIns = new ArrayList<>();
        try {
            carIns = carInDao.findByCarNo(carNo);
            for (CarIn carIn : carIns) {
                if (carIn.getStatus() == Status.exist) {
                    //改为出库状态
                    long id = carIn.getId();
                    try {
                        carInDao.updateStatus(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return saveCarOut(carNo, carIn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private CarOut saveCarOut(String carNo, CarIn carIn) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = new Date();
        CarOut carOut = new CarOut();
        carOut.setCarNo(carNo);
        carOut.setType(carIn.getType());
        carOut.setCarType(carIn.getCarType());
        carOut.setPrice(carIn.getPrice());
        carOut.setStartDate(carIn.getStartDate());
        carOut.setEndDate(endTime);
        carOut.setStartDateStr(carIn.getStartDateStr());
        carOut.setEndDateStr(simpleDateFormat.format(endTime));
        long strtime = 0;
        try {
            strtime = carIn.getStartDate().getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long edtime = endTime.getTime();

        long hours = Math.round((edtime - strtime) / (3600 * 1000 * 24));
        if (hours - 2 <= 0) {
            hours = 1;
        }
        double cost = hours * carIn.getPrice();
        carOut.setCost(cost);
        try {
            carOutDao.save(carOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carOut;
    }

    //明细
    public List<CarOut> getAll(int pageNum, int pageSize, String carNo) {
        List<CarOut> carOuts = new ArrayList<>();
        PageRequest request = this.buildPageRequest(pageNum, pageSize);
        if (carNo.equals("")) {
            try {
                Page<CarOut> cars = carOutDao.findAll(request);
                for (CarOut car : cars) {
                    carOuts.add(car);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            List<CarOut> cars = null;
            try {
                cars = carOutDao.findCarOutsByCarNo(carNo);
                carOuts = cars;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return carOuts;
    }

    private PageRequest buildPageRequest(int pageNumber, int pagzSize) {
        return new PageRequest(pageNumber - 1, pagzSize, null);
    }

    //总数
    public long count(String carNo) {
        long count = 0;
        if (carNo.equals("")) {
            try {
                count = carOutDao.count();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                count = carOutDao.countByCarNo(carNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public Map getDatas() {
        List<Integer> outs = new ArrayList<>();
        List<Integer> ins = new ArrayList<>();
        long usedCarPlace = 0;
        try {
            for (int i = 0; i < 7; i++) {
                int out = carOutDao.countOutTodayn(i).size();
                int in = carInDao.countInTodayn(i).size();
                outs.add(out);
                ins.add(in);
            }
            usedCarPlace = carInDao.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        map.put("outs", outs);
        map.put("ins", ins);
        map.put("usedCarPlace", usedCarPlace);
        return map;
    }

}
