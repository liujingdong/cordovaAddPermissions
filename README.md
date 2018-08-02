---
功能一 权限验证
title: android6.0动态权限验证
description: android6.0以上权限验证的cordova插件
Installation: ionic plugin add https://github.com/liujingdong/cordovaAddPermissions.git
Supported Platforms: android
Usage:
var perArr = ["android.permission.WRITE_EXTERNAL_STORAGE","android.permission.RECORD_AUDIO","android.permission.CAMERA"]
PermissionsPlugin.addPermissions("权限",onPermissionsSuccess,onPermissionsFail,perArr);
onPermissionsSuccess:成功回调方法
onPermissionsFail:失败回调方法

功能二 判断网络及其VPN状态
title: 网络及其VPN状态判断
description: 检查网络状态,判断是否连接了VPN
Installation: ionic plugin add https://github.com/liujingdong/cordovaAddPermissions.git
Supported Platforms: android
Usage:
PermissionsPlugin.addPermissions("VPN",function (result) {
          //成功的回调,VPN_OK为连接了VPN,否则为网络状态
              alert("看看" + result);
          },function (err) {
          //失败的回调
              alert("错误了" + err);
          },null);
		  
		  
功能三 判断应用是否打开通知状态栏
 PermissionsPlugin.addPermissions("isNotificationEnabled", function (result) {
                        if(result == "opened"){//通知状态打开

                        }else if(result == "closed"){//通知状态关闭

                    }, null, null);

功能四 进入设置系统应用权限界面
PermissionsPlugin.addPermissions("notificationSet",null,null,null);
---

