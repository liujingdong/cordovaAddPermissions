---
title: android6.0动态权限验证
description: android6.0以上权限验证的cordova插件
Installation: ionic plugin add https://github.com/liujingdong/cordovaAddPermissions.git
Supported Platforms: android
deleting:
var perArr = ["android.permission.WRITE_EXTERNAL_STORAGE","android.permission.RECORD_AUDIO","android.permission.CAMERA"]
PermissionsPlugin.addPermissions("权限",onPermissionsSuccess,onPermissionsFail,perArr);
onPermissionsSuccess:成功回调方法
onPermissionsFail:失败回调方法
---

