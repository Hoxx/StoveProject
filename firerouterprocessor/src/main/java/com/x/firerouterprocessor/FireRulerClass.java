package com.x.firerouterprocessor;

/*存储注解的类信息*/
public class FireRulerClass {

    private String alias;
    private int aliasHashCode;
    private String classFullName;

    public FireRulerClass(String alias, int aliasHashCode, String classFullName) {
        this.alias = alias;
        this.aliasHashCode = aliasHashCode;
        this.classFullName = classFullName;
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
}
