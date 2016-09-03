/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.ast;

/**
 *
 * @author plankp
 */
public final class EmptyExpr extends Expr {

    private static volatile EmptyExpr instance;

    private EmptyExpr() {
        this.text = "";
    }

    public static EmptyExpr getInstance() {
        EmptyExpr locl_inst = EmptyExpr.instance;
        if (locl_inst == null) {
            synchronized (EmptyExpr.class) {
                locl_inst = EmptyExpr.instance;
                if (locl_inst == null) {
                    EmptyExpr.instance = locl_inst = new EmptyExpr();
                }
            }
        }
        return locl_inst;
    }

    @Override
    public int getChildNodes() {
        return 0;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        return this;
    }
}
