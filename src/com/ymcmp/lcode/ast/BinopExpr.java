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
public class BinopExpr extends Expr {

    private final Expr left, right;
    private final Expr op;

    public BinopExpr(Expr left, String op, Expr right) {
        this.left = left;
        this.op = new OpExpr(op);
        this.right = right;
        this.text = left.getText() + op + right.getText();
    }

    @Override
    public int getChildNodes() {
        return 3;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        switch (nodeIndex) {
        case 0:
            return left;
        case 1:
            return op;
        case 2:
            return right;
        default:
            return EmptyExpr.getInstance();
        }
    }

    @Override
    public String toTree() {
        return String.format("(%s %s %s)", op.toTree(), left.toTree(), right.toTree());
    }
}
