/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.objutils;

/**
 *
 * @author plankp
 */
public interface ObjUtils {

    public ObjUtils is(Object b);

    public ObjUtils or();

    public boolean translate();

    public Object getBaseVal();

    public static ObjUtils apply(Object val) {
        return new ObjUtilsImpl(val);
    }
}

class ObjUtilsTerminalTrue implements ObjUtils {

    private ObjUtilsIs pat;

    @Override
    public ObjUtils is(Object b) {
        return pat = new ObjUtilsIs(true, b);
    }

    @Override
    public ObjUtils or() {
        return this;
    }

    @Override
    public boolean translate() {
        if (pat == null) {
            return true;
        }
        return pat.translate();
    }

    @Override
    public Object getBaseVal() {
        return true;
    }
}

class ObjUtilsImpl implements ObjUtils {

    private final Object a;

    private ObjUtils child;

    public ObjUtilsImpl(Object val) {
        this.a = val;
    }

    @Override
    public Object getBaseVal() {
        return a;
    }

    @Override
    public ObjUtils is(Object b) {
        ObjUtilsIs obj = new ObjUtilsIs(a, b);
        child = obj;
        return obj;
    }

    @Override
    public ObjUtils or() {
        if (child == null) {
            return new ObjUtilsTerminalTrue();
        }
        return child = new ObjUtilsOr(this);
    }

    @Override
    public boolean translate() {
        return child.translate();
    }
}
