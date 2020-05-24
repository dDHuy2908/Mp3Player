package com.huydo2908.basemodule.base;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<BD extends ViewDataBinding> extends AppCompatActivity {

    protected BD binding;
    private RequestPermissionCallBack callBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());

        init();
    }

    protected abstract void init();

    protected abstract int getLayoutId();

    public interface RequestPermissionCallBack {
        void onGranted();

        void onDenied();
    }

    public void doRequestPermission(String[] permission, RequestPermissionCallBack callBack) {
        if (checkPermission(permission)) {
            callBack.onGranted();
        } else {
            this.callBack = callBack;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, 0);
            }
        }
    }

    private boolean checkPermission(String[] permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permission) {
                if (checkSelfPermission(p) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission(permissions)) {
            callBack.onGranted();
        } else {
            callBack.onDenied();
        }
    }
}
