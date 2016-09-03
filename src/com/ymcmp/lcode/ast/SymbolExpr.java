/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.ast;

public class SymbolExpr extends Expr {

    public SymbolExpr(String txt) {
        this.text = txt;
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
