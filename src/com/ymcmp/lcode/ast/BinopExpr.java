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
    }

    @Override
    public String getText() {
        return String.format("%s%s%s", left.getText(), op, right.getText());
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
