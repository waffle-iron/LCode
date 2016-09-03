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
public class FuncExpr extends Expr {

    private static final IndentExpr[] EMPTY_INDENT_EXPR_ARR = new IndentExpr[0];

    private final IndentExpr[] params;
    private final Expr body;

    public FuncExpr(IndentExpr[] params, Expr body) {
        this.params = params;
        this.body = body;
        this.text = Arrays.asList(params)
                .stream().map(Expr::getText)
                .collect(Collectors.joining(" ", "[", ":" + body.text + "]"));
    }

    @Override
    public int getChildNodes() {
        return params.length + 1;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        if (nodeIndex < params.length && nodeIndex > -1) {
            return params[nodeIndex];
        }
        if (nodeIndex == params.length) {
            return body;
        }
        return EmptyExpr.getInstance();
    }

    @Override
    public String toTree() {
        return Arrays.asList(params)
                .stream()
                .map(Expr::toTree)
                .collect(Collectors.joining(" ", "(lambda (", ") " + body.toTree() + ")"));
    }

    public static FuncExpr newParamlessFunction(Expr body) {
        return new FuncExpr(EMPTY_INDENT_EXPR_ARR, body);
    }
}
