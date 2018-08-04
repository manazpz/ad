package aq.common.access;

import aq.common.util.DES;
import aq.common.util.StringUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * 上下文对象 建议放到各自项目的Web层（如果每个Web层处理逻辑有差异的话）
 * Created by 杨大山 on 2017-02-17.
 */
public class Context extends ContextBase{

    //Security Key
    private static final String SECURITY_KEY ="D88F4F270F438B3D2F1D0D2^2B908_11";

    private static Context instance;

    public static synchronized  IContext current() {
       if (instance == null){
           instance = new Context();
       }
       return instance;
    }

    @Override
    public Ticket ticket() {
        try {
            boolean hasToken = false;
            String token = null;
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Cookie[] cookies = request.getCookies();
            if (cookies!=null&&cookies.length >0){
                for (Cookie cookie : cookies){
                    if (cookie.getName().trim().toUpperCase().equals("TOKEN")){
                        hasToken = true;
                        token = URLDecoder.decode(cookie.getValue(),"UTF-8");
                    }
                }
            }
            if (hasToken){
                DES des = new DES(SECURITY_KEY,false);
                if (StringUtil.isEmpty(token)) return null;
                //解密
                String decryptToken = des.decryptString(token);
                Ticket ticket = new Ticket(token,decryptToken.split("\\^")[0], decryptToken.split("\\^")[1]);
                return ticket;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
