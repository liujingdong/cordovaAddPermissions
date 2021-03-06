package custom.cordova.permissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
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
    }else if(action.equals("isNotificationEnabled")){//判断是否打开通知状态栏
      if(isNotificationEnabled()){
        mCallbackContext.success("opened");//打开状态
      }else{
        mCallbackContext.success("closed");//关闭状态
      }
      return true;
    }else if(action.equals("notificationSet")){//设置通知状态栏
      notificationSet();
      return true;
    }else if(action.equals("openPDF")){
      openPdf(args.getString(0));
      return true;
    }else if(action.equals("LevelAPI")){
      getLevelAPI();
      return true;
    }
    return false;
  }

  //获取API level
  private void getLevelAPI() {
    int lever = Build.VERSION.SDK_INT;
    mCallbackContext.success(lever);
  }

  //打开PDF的方法
  private void openPdf(String path) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromFile(new File(path));
    intent.setDataAndType(uri,"application/pdf");
    mCallbackContext.success();
    cordova.getActivity().startActivity(intent);
  }

  //设置通知和状态栏
  private void notificationSet() {
    // 进入设置系统应用权限界面
    Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.fromParts("package", cordova.getActivity().getPackageName(), null));
    cordova.getActivity().startActivity(intent);
  }

  //判断通知状态栏是否打开
  private boolean isNotificationEnabled() {
    NotificationManagerCompat manger = NotificationManagerCompat.from(cordova.getActivity());
    boolean isOpende = manger.areNotificationsEnabled();
    return isOpende;
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
