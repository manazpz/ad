package aq.controller.restful.management;

import aq.common.util.HttpUtil;
import aq.service.system.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
@RequestMapping("/user")
public class UserController extends aq.controller.restful.System {
    @Resource
    protected UserService userService;

}
