/*
 * The MIT License
 *
 * Copyright 2016 Paul T.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
        }

        @Override
        public String getText() {
            return String.format("%s:%s", left.getText(), right.getText());
        }

        @Override
        public int getChildNodes() {
            return 2;
        }

        @Override
        public Expr getChildNode(int nodeIndex) {
            switch (nodeIndex) {
            case 0:
                return left;
            case 1:
                return right;
            default:
                return EmptyExpr.getInstance();
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
    }

    @Override
    public String getText() {
        return Arrays.asList(exprs)
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
            return "(map)";
        }
        return Arrays.asList(exprs)
                .stream()
                .map(Expr::toTree)
                .collect(Collectors.joining(" ", "(map ", ")"));
    }
}
