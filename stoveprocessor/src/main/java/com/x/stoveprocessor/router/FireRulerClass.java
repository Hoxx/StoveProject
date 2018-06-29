package com.x.stoveprocessor.router;

/*存储注解的类信息*/
public class FireRulerClass {

    private String alias;
    private int aliasHashCode;
    private String classFullName;
    private String packageName;

    FireRulerClass(String alias, int aliasHashCode, String classFullName, String packageName) {
        this.alias = alias;
        this.aliasHashCode = aliasHashCode;
        this.classFullName = classFullName;
        this.packageName = packageName;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public String getAlias() {
        return alias;
    }

    public int getAliasHashCode() {
        return aliasHashCode;
    }

    public String getPackageName() {
        return packageName;
    }
}
