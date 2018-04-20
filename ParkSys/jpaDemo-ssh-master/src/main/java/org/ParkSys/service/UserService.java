package org.ParkSys.service;

import org.ParkSys.bean.User;
import org.ParkSys.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    //登录
    public User login(String userName, String passWd) {
        User user = null;
        String pwd = null;
        try {
            user = userDao.findByUserName(userName);
            if (user == null) {
                return user;
            }
            pwd = user.getPassWd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (passWd.equals(pwd)) {
            return user;
        } else {
            return null;
        }
    }

    //新建
    public String creatUser(String username, String passwd, String nickName, String phoneNo, String memo) {
        User user = new User();
        user.setUserName(username);
        user.setPassWd(passwd);
        user.setNickName(nickName);
        user.setPhoneNo(phoneNo);
        user.setMemo(memo);
        User ur = null;
        try {
            User oldUser = userDao.findByUserName(username);
            if (oldUser != null) {
                return "2";
            }
            ur = userDao.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ur != null) {
            return "1";
        } else {
            return "0";
        }
    }

    //删除
    public boolean delUser(long id) {
        boolean isSuccess = false;
        try {
            userDao.delete(id);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    //查询
    public User findByName(String name) {
        User user = null;
        try {
            user = userDao.findByUserName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public String modifyPwd(long id, String oldPwd, String newPwd) {
        User user = null;
        try {
            user = userDao.findOne(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            if (user.getPassWd().equals(oldPwd)) {
                userDao.updatePwd(id, newPwd);
                return "1";
            } else {
                return "0";
            }
        } else {
            return null;
        }
    }

    //修改
    public boolean updateUser(long id, String userName, String nickName, String phoneNo, String memo) {
        User user = null;
        boolean isSuccess = false;
        try {
            user = userDao.findOne(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            user.setUserName(userName);
            user.setNickName(nickName);
            user.setPhoneNo(phoneNo);
            user.setMemo(memo);
            try {
                userDao.save(user);
                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    //getALL
    public List<User> getAll(int pageNum, int pageSize, String userName) {
        List<User> users = new ArrayList<>();
        PageRequest request = this.buildPageRequest(pageNum, pageSize);
        if (userName.equals("")) {
            try {
                Page<User> userPage = userDao.findAll(request);
                for (User user : userPage) {
                    users.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            User user = null;
            try {
                user = userDao.findByUserName(userName);
                users.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    private PageRequest buildPageRequest(int pageNumber, int pagzSize) {
        return new PageRequest(pageNumber - 1, pagzSize, null);
    }

    public long count(String userName) {
        long count = 0;
        if (userName.equals("")) {
            try {
                count = userDao.count();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                count = userDao.countByUserName(userName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }
}
