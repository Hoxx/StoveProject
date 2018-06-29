package com.x.stoveprocessor.findid;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Name;

/*对应一个注解的View*/
public class StoveFindIdView {

    private Name viewName;
    private TypeName viewTypeName;
    private boolean isFragment;


    public void setViewName(Name viewName) {
        this.viewName = viewName;
    }

    public void setViewTypeName(TypeName viewTypeName) {
        this.viewTypeName = viewTypeName;
    }

    public Name getViewName() {
        return viewName;
    }

    public TypeName getViewTypeName() {
        return viewTypeName;
    }

    public boolean isFragment() {
        return isFragment;
    }

    public void setFragment(boolean fragment) {
        isFragment = fragment;
    }
}
