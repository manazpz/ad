package aq.app.vue.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RequestFilter extends aq.common.interceptor.RequestFilter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isAccessed =  super.preHandle(request,response,handler),isLogin = false,canContinue=true;
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        PrintWriter out = response.getWriter();

        //记录日志  +  异常处理
        if (ex !=null){
            out.println("{\"result\":4}");   //4 发生异常
        }

        out.close();
    }

}
