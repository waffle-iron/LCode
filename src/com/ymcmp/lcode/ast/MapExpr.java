
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.ast;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author plankp
 */
public class MapExpr extends Expr {

    public static class ExprPair extends Expr {

        private final Expr left, right;

        public ExprPair(Expr left, Expr right) {
            this.left = left;
            this.right = right;
            this.text = left.getText() + ":" + right.getText();
        }

        @Override
        public int getChildNodes() {
            return 2;
        }

        @Override
        public Expr getChildNode(int nodeIndex) {
            switch (nodeIndex) {
            case 0: return left;
            case 1: return right;
            default: return EmptyExpr.getInstance();
            }
        }

        @Override
        public String toTree() {
          return String.format("(pair %s %s)", left.toTree(), right.toTree());
        }
    }

    private final ExprPair[] exprs;

    public MapExpr(ExprPair... exprs) {
        this.exprs = exprs;
        this.text = Arrays.asList(exprs)
                .stream()
                .map(ExprPair::getText)
                .collect(Collectors.joining(" ", "{", "}"));
    }

    @Override
    public int getChildNodes() {
        return exprs.length;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        return nodeIndex < exprs.length && nodeIndex > -1
                ? exprs[nodeIndex]
                : EmptyExpr.getInstance();
    }

    @Override
    public String toTree() {
        if (exprs.length == 0) {
            return "(list)";
        }
        return Arrays.asList(exprs)
                .stream()
                .map(Expr::toTree)
                .collect(Collectors.joining(" ", "(map ", ")"));
    }
}
