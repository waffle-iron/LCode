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
public class ListExpr extends Expr {

    private final Expr[] exprs;

    public ListExpr(Expr... exprs) {
        this.exprs = exprs;
        this.text = Arrays.asList(exprs)
                .stream()
                .map(Expr::getText)
                .collect(Collectors.joining(" ", "'(", ")"));
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
                .collect(Collectors.joining(" ", "(list ", ")"));
    }
}
