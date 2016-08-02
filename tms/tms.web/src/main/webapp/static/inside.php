<?php
/**
 * The router file of ZenTaoPHP.
 *
 * All request should be routed by this router.
 *
 * The author disclaims copyright to this source code.  In place of
 * a legal notice, here is a blessing:
 * 
 *  May you do good and not evil.
 *  May you find forgiveness for yourself and forgive others.
 *  May you share freely, never taking more than you give.
 */
error_reporting(E_ALL);

/* Load the framework. */
include '../../../framework/cat.php';
/* Instance the app. */
$app = router::createApp('demo', dirname(dirname(__FILE__)));
/* Run the app. */
$common = $app->loadCommon();
$app->parseRequest();
$common->validate();
$app->loadModule();
session_write_close();