/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.objutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author plankp
 */
class ObjUtilsIs implements ObjUtils {

    private final List<Object> cmpList = new ArrayList<>();

    private ObjUtils parentNode;

    public ObjUtilsIs(Object a, Object b) {
        this(null, a, b);
    }

    public ObjUtilsIs(ObjUtils parent, Object a, Object b) {
        parentNode = parent;
        this.cmpList.add(a);
        this.cmpList.add(b);
    }

    @Override
    public ObjUtils is(Object b) {
        this.cmpList.add(b);
        return this;
    }

    @Override
    public ObjUtils or() {
        return new ObjUtilsOr(this);
    }

    @Override
    public Object getBaseVal() {
        return cmpList.get(0);
    }

    @Override
    public boolean translate() {
        if (parentNode != null) {
            // Used for conditions like Or
            final ObjUtils tmp = parentNode;
            parentNode = null; // parent *translate* this will call the other part
            final boolean ret = tmp.translate();
            parentNode = tmp; // Restore old parentNode
            return ret;
        }
        boolean ret = true;
        final Object first = cmpList.get(0);
        final int size = cmpList.size();
        for (int i = 1; i < size; i++) {
            if (!ret) {
                break;
            }
            ret = Objects.equals(first, cmpList.get(i));
        }
        return ret;
    }

}
