package custom.cordova.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by CrazyDong on 2017/11/2.
 *android6.0以上添加权限插件
 */
public class PermissionsPlugin extends CordovaPlugin {
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
    }
    return false;
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
  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
    super.onRequestPermissionResult(requestCode, permissions, grantResults);
    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
      //用户允许
    }else {
      Toast.makeText(cordova.getActivity(),"您已决绝",Toast.LENGTH_SHORT).show();
    }
  }
}
