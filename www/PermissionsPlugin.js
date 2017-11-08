/**
 * Created by CrazyDong on 2017/11/2.
 */
var exec = require('cordova/exec');

module.exports = {
    /*调用对比
     * js: cordova.exec(callbackContext.success, callbackContext.error, PluginName, action, args);
     * java: public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
     * */
    addPermissions:function(action,success,err,args){
        exec(success,err, "PermissionsPlugin", action, args);
    }
}