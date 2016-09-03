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
public class IndentExpr extends Expr {

    private boolean ignoredIndent;

    public IndentExpr(String val) {
        this.text = val;
    }

    public IndentExpr(String val, boolean isIgnoredIndent) {
        this.text = val;
        this.ignoredIndent = isIgnoredIndent;
    }

    @Override
    public int getChildNodes() {
        return 0;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        return EmptyExpr.getInstance();
    }

    public boolean isIgnoredIndent() {
        return ignoredIndent;
    }
}
