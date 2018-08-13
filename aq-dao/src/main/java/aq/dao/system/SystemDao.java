package aq.dao.system;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;
/*
* 系统用户、角色、权限、登录校验等相关
* */
@MapperScan("daoSystem")
public interface SystemDao {

    //获取令牌
    List<Map<String,Object>> selectSysToken(Map<String,Object> map);

    //删除令牌
    int deleteSytToken(Map<String,Object> map);

    //插入令牌
    int insertSytToken(Map<String,Object> map);

    //更新令牌
    int updateSytToken(Map<String,Object> map);

    //登录
    List<Map<String,Object>> selectSysLogin(Map<String,Object> map);

    //查询用户基本信息
    List<Map<String,Object>> selectUserInfo(Map<String,Object> map);

    //查询用户拥有的权限
    List<Map<String,Object>> selectSysPermissionUser(Map<String,Object> map);

    //权限查询
    List<Map<String,Object>> selectSysPermissionInfo(Map<String,Object> map);






























    //权限-新增
    int insertSysPermission(Map<String,Object> map);






















    //region 用户
    //用户-查询
    List<Map<String,Object>> queryUser(Map<String,Object> map);
    //endregion

    //region 组织
    //组织-查询
    List<Map<String,Object>> queryOrganization(Map<String,Object> map);

    //endregion

    //获取用户信息
    List<Map<String,Object>> selectSysOrgPersonByUserName(Map<String,Object> map);

    //获取登录ticket
    List<Map<String,Object>> selectSysSecurityById(Map<String,Object> map);

    //删除令牌
    int deleteSysSecurityByUid(Map<String,Object> map);

    //用户权限
    List<Map<String,Object>> selectSysUserPermission(Map<String,Object> map);

    //获取用户信息
    List<Map<String,Object>> selectSysOrgElementByUid(Map<String,Object> map);

    //微信用户分页查询
    List<Map<String,Object>> selectUserInfoPage(Map<String,Object> map);

    //微信用户-更新
    int updateZdevUserInfo(Map<String,Object> map);

    //微信用户-新增
    int insertUserInfo1(Map<String,Object> map);

    //角色分页查询
    List<Map<String,Object>> selectSysRole(Map<String,Object> map);

    //用户添加角色
    int insertSysRoleUser(Map<String,Object> map);

    //查询用户拥有的角色
    List<Map<String,Object>> selectSysRoleUser(Map<String,Object> map);

    //删除用户拥有的角色
    int deleteSysRoleUser(Map<String, Object> map);

    //角色-更新
    int updateSysRole(Map<String,Object> map);

    //角色-新增
    int insertSysRole(Map<String,Object> map);

    //角色分配权限
    int insertSysPermissionRole(Map<String,Object> map);




    //权限-更新
    int updateSysPermission(Map<String,Object> map);



    //用户添加权限
    int insertSysPermissionUser(Map<String,Object> map);



    //删除用户拥有的权限
    int deleteSysPermissionUser(Map<String, Object> map);

    //查询角色拥有的权限
    List<Map<String,Object>> selectSysPermissionRole(Map<String,Object> map);

    //删除角色权限
    int deleteSysPermissionRole(Map<String,Object> map);

    //同步数据-新增
    int insertSysSync(Map<String,Object> map);

    //同步数据-修改
    int updateSysSync(Map<String,Object> map);

    //同步数据-查询
    List<Map<String,Object>> selectSysSync(Map<String,Object> map);

    //查询同步日志错误信息
    List<Map<String,Object>> selectSysErrorMsg(Map<String,Object> map);

    //查询微信成员权限
    List<Map<String,Object>> selectWxPermission(Map<String,Object> map);

}
