package com.example.rootdetectorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBoxKnownRootFiles, checkBoxSUBinary, checkBoxBusyBox,
            checkBoxRootApps, checkBoxSystemIntegrity, checkBoxMagisk, checkBoxNativeCheck;
    private Button buttonCheckRoot;

    // Native method declaration
    public native boolean checkRootNative();

    // Load the native library
    static {
        System.loadLibrary("root_check");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        checkBoxKnownRootFiles = findViewById(R.id.checkBoxKnownRootFiles);
        checkBoxSUBinary = findViewById(R.id.checkBoxSUBinary);
        checkBoxBusyBox = findViewById(R.id.checkBoxBusyBox);
        checkBoxRootApps = findViewById(R.id.checkBoxRootApps);
        checkBoxSystemIntegrity = findViewById(R.id.checkBoxSystemIntegrity);
        checkBoxMagisk = findViewById(R.id.checkBoxMagisk);
        checkBoxNativeCheck = findViewById(R.id.checkBoxNativeCheck);
        buttonCheckRoot = findViewById(R.id.buttonCheckRoot);

        buttonCheckRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRootChecks();
            }
        });
    }

    private void performRootChecks() {
        boolean knownRootFiles = checkForKnownRootFiles();
        boolean suBinary = checkForSUBinary();
        boolean busyBox = checkForBusyBox();
        boolean rootApps = checkForRootManagementApps();
        boolean systemIntegrityCompromised = !verifySystemIntegrity();
        boolean magiskDetected = searchForMagisk();
        boolean nativeRootDetected = checkRootNative();

        checkBoxKnownRootFiles.setChecked(knownRootFiles);
        checkBoxSUBinary.setChecked(suBinary);
        checkBoxBusyBox.setChecked(busyBox);
        checkBoxRootApps.setChecked(rootApps);
        checkBoxSystemIntegrity.setChecked(systemIntegrityCompromised);
        checkBoxMagisk.setChecked(magiskDetected);
        checkBoxNativeCheck.setChecked(nativeRootDetected);

        boolean isRooted = knownRootFiles || suBinary || busyBox || rootApps || magiskDetected || nativeRootDetected;

        if (isRooted) {
            showRootedWarning();
        } else {
            showNotRootedMessage();
        }
    }

    private boolean checkForKnownRootFiles() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private boolean checkForSUBinary() {
        return checkCommand("which su");
    }

    private boolean checkForBusyBox() {
        return checkCommand("which busybox");
    }

    private boolean checkForRootManagementApps() {
        return packageExists("com.noshufou.android.su") ||
                packageExists("com.thirdparty.superuser") ||
                packageExists("eu.chainfire.supersu") ||
                packageExists("com.topjohnwu.magisk");
    }

    private boolean verifySystemIntegrity() {
        String buildTags = android.os.Build.TAGS;
        return buildTags == null || !buildTags.contains("test-keys");
    }

    private boolean searchForMagisk() {
        return new File("/sbin/.magisk").exists() ||
                new File("/sbin/.core/mirror").exists() ||
                packageExists("com.topjohnwu.magisk");
    }

    private boolean checkCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    private boolean packageExists(String targetPackage) {
        try {
            getPackageManager().getPackageInfo(targetPackage, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void showRootedWarning() {
        new AlertDialog.Builder(this)
                .setTitle("Root Detected")
                .setMessage("This device appears to be rooted. The app will now close.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showNotRootedMessage() {
        new AlertDialog.Builder(this)
                .setTitle("No Root Detected")
                .setMessage("This device does not appear to be rooted.")
                .setPositiveButton("OK", null)
                .show();
    }
}