package com.x.firerouter;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FireRouterUtil {


    public static Set<String> getAppAllActivityList(PackageManager packageManager, String currentPackageName) {
        //搜索APP的所有Activity
        List<PackageInfo> list = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        Set<String> pkgNameSet = new HashSet<>();
        for (PackageInfo info : list) {
            if (info.packageName.equals(currentPackageName)) {
                for (ActivityInfo activityInfo : info.activities) {
                    String packageName = activityInfo.name.substring(0, activityInfo.name.lastIndexOf("."));
                    add(pkgNameSet, packageName);
                }
            }
        }
        return pkgNameSet;
    }

    private static void add(Set<String> pkgNameSet, String pkgName) {
        if (pkgNameSet.size() <= 0) {
            pkgNameSet.add(pkgName);
            return;
        }
        for (Iterator<String> iterable = pkgNameSet.iterator(); iterable.hasNext(); ) {
            String name = iterable.next();
            if (name.equals(pkgName)) return;
            if (name.length() < pkgName.length() && pkgName.contains(name)) return;
            if (name.length() > pkgName.length() && name.contains(pkgName)) {
                iterable.remove();
            }
        }
        pkgNameSet.add(pkgName);
    }

}
