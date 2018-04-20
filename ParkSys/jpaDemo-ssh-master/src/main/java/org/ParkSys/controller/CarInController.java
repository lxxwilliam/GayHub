package org.ParkSys.controller;

import org.ParkSys.enums.CarType;
import org.ParkSys.service.CarInService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class CarInController {

    @Resource
    private CarInService carInService;
//入库数据读取是从出库表读取的 不满足实时性 是否需要修改表结构

    @RequestMapping("carIn")
    @ResponseBody
    public Map carIn(HttpServletRequest request) {
        String num = request.getParameter("carNo");
        String carNo = num.toUpperCase();
        String type = request.getParameter("carType");
        CarType carType=CarType.small;
        if ("1".equals(type)) {
            carType = CarType.middle;
        } else if ("2".equals(type)) {
            carType = CarType.big;
        }
        Map map = new HashMap();
        if (carInService.saveCar(carNo, carType)) {
            map.put("msg","1");
        } else {
            map.put("msg","0");
        }
        return map;
    }
}
