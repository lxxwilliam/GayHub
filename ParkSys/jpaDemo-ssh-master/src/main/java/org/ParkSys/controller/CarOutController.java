package org.ParkSys.controller;

import org.ParkSys.bean.CarOut;
import org.ParkSys.service.CarInService;
import org.ParkSys.service.CarOutService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class CarOutController {

    @Resource
    private CarOutService carOutService;


    @RequestMapping("carOut")
    @ResponseBody
    public Map carOut(HttpServletRequest request) {
        String carno = request.getParameter("carNo");
        CarOut carout = carOutService.getCar(carno);
        List cars = new ArrayList();
        Map map = new HashMap();
        map.put("code", 0);
        map.put("count", 1);
        if (carout == null) {
            map.put("msg", "0");
            map.put("data", cars);
        } else {
            map.put("msg", "1");
            cars.add(carout);
            map.put("data", cars);
        }
        return map;
    }

    @RequestMapping("getRecords")
    @ResponseBody
    public Map getAll(HttpServletRequest request) {
        String numAndSize = request.getQueryString();
        String[] parms = numAndSize.split("&");
        int num = Integer.parseInt(parms[0].substring(parms[0].indexOf("=") + 1, parms[0].length()));
        int size = Integer.parseInt(parms[1].substring(parms[1].indexOf("=") + 1, parms[1].length()));
        String carNo = "";
        if (parms.length > 2) {
            carNo = parms[2].substring(parms[2].indexOf("=") + 1, parms[2].length());
        }
        List<CarOut> carOuts = carOutService.getAll(num, size, carNo);
        long count = carOutService.count(carNo);
        Map map = new HashMap();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", carOuts);
        return map;
    }

    @RequestMapping("getDatas")
    @ResponseBody
    public Map getDatas() {
        Map map = new HashMap();
        try {
            map = carOutService.getDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
