package com.chinaway.tms.util;

public class Constants {
	
private static volatile Constants constant = null;
    
    private Constants(){}
    
    public static Constants getConstantn(){
        if(constant == null){
            synchronized (Constants.class){
                if(constant == null){
                    constant = new Constants();
                }
            }
        }
        return constant;
    }
    
    /**
     * 登录异常
     */
    public static String LOGIN_EXCEPTION="登录异常！";
    
    /**
     * 登录成功
     */
    public static String LOGIN_SUCCESS="登录成功！";

    /**
     * 用户名或密码错误
     */
    public static String USER_OR_PASSWORD_ERROR="用户名或密码不正确!";
    
    /**
     * 根据条件查询部门操作失败
     */
    public static String BY_CTN_QUERY_DEPT_FAILED = "根据条件查询部门操作失败!";
   
    /**
     * 根据条件查询部门操作失败
     */
    public static String BY_CTN_QUERY_DEPT_SUCCESS = "根据条件查询部门操作成功!";
    
    /**
     * 根据条件查询部门操作失败
     */
    public static String QUERY_ALL_DEPT_FAILED = "查询所有部门操作失败!";
    
    /**
     * 查询所有部门操作失败
     */
    public static String QUERY_ALL_DEPT_SUCCESS = "查询所有部门操作成功!";
    
    /**
     * 获取菜单异常
     */
    public static String GET_MENU_EXCEPTION = "获取菜单异常!";
    
    /**
     * 获取菜单成功
     */
    public static String GET_MENU_SUCCESS = "获取菜单成功!";
    
    /**
     * 获取菜单失败
     */
    public static String GET_MENU_ERROR = "获取菜单不正确!";
    
    /**
     * 注销异常
     */
    public static String LOGOUT_EXCEPTION = "注销异常!";
    
    /**
     * 注销成功
     */
    public static String LOGOUT_SUCCESS = "注销成功!";
    
    /**
     * 注销失败
     */
    public static String LOGOUT_ERROR = "用户注销不正确!";
    
    /**
     * 登录校验异常
     */
    public static String IS_LOGIN_EXCEPTION = "登录校验异常!";
    
    /**
     * 登录校验异常
     */
    public static String USER_NOT_LOGIN = "用户未登录!";
    
    /**
     * 登录校验异常
     */
    public static String QUERY_ALL_USER_OPRATION_FAILED = "查询所有用户操作失败!";
    
    /**
     * 登录校验异常
     */
    public static String QUERY_ALL_USER_OPRATION_SUCCESS = "查询所有用户操作成功!";
    
    /**
     * 登录校验异常
     */
    public static String QUERY_ALL_USER_OPRATION_EXCEPTION = "查询所有用户操作出现异常!";
    
    /**
     * 登录校验异常
     */
    public static String BY_ID_QUERY_USER_OPRATION_FAILED = "根据id查询用戶操作失败!";
    
    /**
     * 根据id查询用戶操作成功
     */
    public static String BY_ID_QUERY_USER_OPRATION_SUCCESS = "根据id查询用戶操作成功!";
    
    /**
     * 添加操作失败
     */
    public static String ADD_OPRATION_FAILED = "添加操作失败!";
    
    /**
     * 修改操作成功
     */
    public static String UPDATE_OPRATION_SUCCESS = "修改操作成功!";
    
    /**
     * 添加操作成功
     */
    public static String ADD_OPRATION_SUCCESS = "添加操作成功!";
    
    /**
     * 删除操作失败
     */
    public static String DELETE_OPRATION_FAILED = "删除操作失败!";
    
    /**
     * 删除操作成功
     */
    public static String DELETE_OPRATION_SUCCESS = "删除操作成功!";
    
    /**
     * 修改用户操作成功
     */
    public static String UPDATE_USER_OPRATION_SUCCESS = "修改用户操作成功!";
    
    /**
     * 修改用户操作失败
     */
    public static String UPDATE_USER_OPRATION_FAILED = "修改用户操作失败!";
    
    /**
     * 修改用户操作失败
     */
    public static String ADD_USER_ROLE_OPRATION_FAILED = "添加用户角色操作失败!";
    
    /**
     * 修改用户操作失败
     */
    public static String ADD_USER_ROLE_OPRATION_SUCCESS = "添加用户角色操作成功!";
    
    /**
     * 修改用户操作失败
     */
    public static String DELETE_USER_ROLE_OPRATION_FAILED = "删除用户角色操作失败!";
    
    /**
     * 修改用户操作失败
     */
    public static String DELETE_USER_ROLE_OPRATION_SUCCESS = "删除用户角色操作成功!";
    
    /**
     * 添加角色菜单操作失败
     */
    public static String ADD_ROLE_MENU_OPRATION_FAILED = "添加角色菜单操作失败!";
    
    /**
     * 添加角色菜单操作成功
     */
    public static String ADD_ROLE_MENU_OPRATION_SUCCESS = "添加角色菜单操作成功!";
    
    /**
     * 删除角色菜单操作失败
     */
    public static String DELETE_ROLE_MENU_OPRATION_FAILED = "删除角色菜单操作失败!";
    
    /**
     * 删除角色菜单操作成功
     */
    public static String DELETE_ROLE_MENU_OPRATION_SUCCESS = "删除角色菜单操作成功!";
    
    /**
     * 根据id查询角色操作失败
     */
    public static String BY_ID_QUERY_ROLE_OPRATION_FAILED = "根据id查询角色操作失败!";
    
    /**
     * 根据id查询角色操作失败
     */
    public static String BY_ID_QUERY_ROLE_OPRATION_SUCCESS = "根据id查询角色操作失败!";
   
    /**
     * 修改角色操作失败
     */
    public static String UPDATE_ROLE_OPRATION_FAILED = "修改角色操作失败!";
    
    /**
     * 修改角色操作成功
     */
    public static String UPDATE_ROLE_OPRATION_SUCCESS = "修改角色操作成功!";
    
    /**
     * 添加菜单操作失败
     */
    public static String ADD_MENU_OPRATION_FAILED = "添加菜单操作失败!";
    
    /**
     * 添加菜单操作成功
     */
    public static String ADD_MENU_OPRATION_SUCCESS = "添加菜单操作成功!";
    
    /**
     * 批量删除操作失败
     */
    public static String BATH_DELETE_OPRATION_FAILED = "批量删除操作失败!";
    
    /**
     * 批量删除操作成功
     */
    public static String BATH_DELETE_OPRATION_SUCCESS = "批量删除操作成功!";
    
    /**
     * 修改菜单操作失败
     */
    public static String UPDATE_MENU_OPRATION_FAILED = "修改菜单操作失败!";
    
    /**
     * 修改菜单操作成功
     */
    public static String UPDATE_MENU_OPRATION_SUCCESS = "修改菜单操作成功!";
    
    /**
     * 操作成功
     */
    public static String OPRATION_SUCCESS = "操作成功!";
    
    /**
     * 删除部门操作失败
     */
    public static String DELETE_DEPT_OPRATION_FAILED = "删除部门操作失败!";
    
    /**
     * 删除部门操作成功
     */
    public static String DELETE_DEPT_OPRATION_SUCCESS = "删除部门操作成功!";
    
    /**
     * 修改部门操作失败
     */
    public static String UPDATE_DEPT_OPRATION_FAILED = "修改部门操作失败!";
    
    /**
     * 修改部门操作成功
     */
    public static String UPDATE_DEPT_OPRATION_SUCCESS = "修改部门操作成功!";
    
    
}
