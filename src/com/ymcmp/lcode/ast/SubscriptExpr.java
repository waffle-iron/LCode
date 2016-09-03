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
public class SubscriptExpr extends Expr {

    private final Expr subscriptBase;
    private final Expr subspos;

    public SubscriptExpr(Expr subscriptBase, Expr subspos) {
        this.subscriptBase = subscriptBase;
        this.subspos = subspos;
        this.text = subscriptBase.getText() + "->" + subspos.getText();
    }

    @Override
    public int getChildNodes() {
        return 2;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        switch (nodeIndex) {
        case 0:
            return subscriptBase;
        case 1:
            return subspos;
        default:
            return EmptyExpr.getInstance();
        }
    }

    @Override
    public String toTree() {
        return String.format("(subs %s %s)", subscriptBase.toTree(), subspos.toTree());
    }
}
