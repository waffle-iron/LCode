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
