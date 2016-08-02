<?php
/**
 * 接口入口文件
 *
 * @author Farmer.Li <me@farmerli.com>
 */
error_reporting(E_ALL);

/* Load the framework. */
include '../../../../framework/cat.php';

try {
    define('IS_RESTFULL', true);
    /* Instance the app. */
    $app = router::createApp('demo', dirname(dirname(dirname(__FILE__))));
    $orgroot=$app->config->orgCode;
    //手机客户端访问用户
    Ucenter::init()->setMockUser($orgroot,'orgcode');
    $app->user = Ucenter::init()->getUserInfo();
    /* Run the app. */
    $common = $app->loadCommon();

    $methods = explode('.', $_REQUEST['method']);

    $moduleName = $methods[1];
    $methodName = $methods[2];
    $format = isset($_REQUEST['format']) ? $_REQUEST['format'] : 'json';
    $_GET[$config->moduleVar] = $moduleName; 
    $_GET[$config->methodVar] = $methodName; 
    $_GET[$config->viewVar] = $format;
    
    $app->parseRequest();

    $app->loadModule();
} catch (Exception $e) {
    // 输出JSON格式错误(只要code和message) 2013-05-08 by lizw
    $ecode = $e->getCode();
    $result = array(
            'code' => $ecode,
            'message' => sprintf( "Exception:  %s", $e->getMessage() )
    );
    if($ecode!=403) {
        // $_pp = helper::filterParams(1);
        // helper::datalog('inside|'.$ecode.'|'.$e->getMessage().'|'.$e->getTraceAsString().'|'.var_export($_pp,true),'syslog_');
    }

    header("Content-Type:   application/json");
    $errorstring = json_encode($result);
    log::error('Code : ' . $e->getCode() . 'Exception: ' . $e->getMessage(), true, true);
    $errorstring = str_replace(array('60.28.206.72','60.28.206.69','ips','221','gps','211.100.54.133','330','gprs'), array(), $errorstring);
    echo $errorstring;
}   
session_write_close();