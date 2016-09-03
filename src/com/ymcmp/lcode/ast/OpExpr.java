/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.ast;

public class OpExpr extends Expr {

    public OpExpr(String op) {
        this.text = op;
    }

    @Override
    public int getChildNodes() {
        return 0;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        return EmptyExpr.getInstance();
    }

}
