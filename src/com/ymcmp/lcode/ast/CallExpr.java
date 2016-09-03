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
public class CallExpr extends Expr {

    private final Expr selector;
    private final Expr[] args;

    public CallExpr(Expr selector, Expr... args) {
        this.selector = selector;
        this.args = args;
        this.text = Arrays.asList(args)
                .stream()
                .map(Expr::getText)
                .collect(Collectors.joining(" ", selector.getText() + "(", ")"));
    }

    @Override
    public int getChildNodes() {
        return args.length + 1;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        if (nodeIndex == 0) {
            return selector;
        }
        if (nodeIndex > 0 && nodeIndex <= args.length) {
            return args[nodeIndex - 1];
        }
        return EmptyExpr.getInstance();
    }

    @Override
    public String toTree() {
        return String.format("(%s %s)", selector.toTree(), Arrays.asList(args)
                             .stream()
                             .map(Expr::toTree)
                             .collect(Collectors.joining(" ")));
    }
}
