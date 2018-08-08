package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.UserService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceUser")
@DyncDataSource
public class UserServiceImpl extends BaseServiceImpl implements UserService{

    @Resource
    private UserDao userDao;


    @Override
    public JsonObject insertUserInfo(JsonObject jsonObject) {
        return null;
    }
}
