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
public class IntExpr extends NumberExpr {

    public IntExpr(String val) {
        this.text = val;
    }

    @Override
    public int getChildNodes() {
        return 1;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        return nodeIndex == 0 ? this : EmptyExpr.getInstance();
    }
}
