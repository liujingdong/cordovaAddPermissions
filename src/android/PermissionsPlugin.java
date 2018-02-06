package custom.cordova.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by CrazyDong on 2017/11/2.
 *android6.0以上添加权限插件
 */
public class PermissionsPlugin extends CordovaPlugin implements ActivityCompat.OnRequestPermissionsResultCallback {
  private CallbackContext mCallbackContext;
  private int requestCode = 201711;
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
    throws JSONException {
    mCallbackContext = callbackContext;
    if(action.equals("权限")){
      String preArr[] = new String[args.length()];
      for (int i = 0; i < args.length(); i++) {
        preArr[i] = args.getString(i);
      }
//      checkPermission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);
      checkPermission(preArr);

      return true;
    }else if(action.equals("VPN")){
      isVpnUsed();
    }
    return false;
  }

  /*判断VPN方法*/
  public void isVpnUsed() {
    boolean isVpn = false;
    //检查网络是否链接
    ConnectivityManager connectivityManager = (ConnectivityManager) cordova.getActivity()
                                              .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    //判断时候有网络
    if(networkInfo != null){
        try {
          Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
          if(niList != null) {
            for (NetworkInterface intf : Collections.list(niList)) {
              if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                continue;
              }
              if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                isVpn = true;
                mCallbackContext.success("VPN_OK");
              }
            }

            if(!isVpn){
              mCallbackContext.success(networkInfo.getTypeName());//返回网络类型
            }

          }
        } catch (Throwable e) {
          mCallbackContext.error("err");
        }

      }else{
        mCallbackContext.error("null_network");
    }

  }


  /**
   * 检测权限
   */
  public void checkPermission(String... permission) {
    if (hasGrantedPermission(permission)) {
      mCallbackContext.success("permission allow");
    } else {
      requestPermission(permission);
    }
  }

  /**
   * 是否已获得传入的权限
   */
  private boolean hasGrantedPermission(String... permission) {
    if (Build.VERSION.SDK_INT > 22) {
      for (String per : permission) {
        boolean hasPers = ContextCompat.checkSelfPermission(
          cordova.getActivity(), per) == PackageManager.PERMISSION_GRANTED;

        if (!hasPers) {
          return false;
        }
      }
    } else {
      return true;
    }
    return true;
  }

  /**
   * 申请权限
   */
  private void requestPermission(String... permission) {

    if (permission == null) {
      return;
    }
    String[] perms = new String[permission.length];
    for (int i = 0; i < permission.length; i++) {
      perms[i] = permission[i];
    }
    ActivityCompat.requestPermissions(cordova.getActivity(), perms, requestCode);
  }


  /*用户操作后回调*/
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
      //用户允许
      mCallbackContext.success("allow");
    }else {
      mCallbackContext.error("reject");
//       Toast.makeText(cordova.getActivity(),"您已决绝",Toast.LENGTH_SHORT).show();
    }
  }
}
