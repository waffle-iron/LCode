/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.objutils;

import java.util.ArrayList;
import java.util.List;

class ObjUtilsOr implements ObjUtils {

    private final List<ObjUtils> cmpList = new ArrayList<>();

    public ObjUtilsOr(ObjUtils baseVal) {
        cmpList.add(baseVal);
    }

    @Override
    public Object getBaseVal() {
        return cmpList.get(0).getBaseVal();
    }

    @Override
    public ObjUtils is(Object b) {
        ObjUtils inst = new ObjUtilsIs(this, getBaseVal(), b);
        cmpList.add(inst);
        return inst;
    }

    @Override
    public ObjUtils or() {
        return this;
    }

    @Override
    public boolean translate() {
        for (int i = 0; i < cmpList.size(); i++) {
            if (cmpList.get(i).translate()) {
                return true;
            }
        }
        return false;
    }

}
