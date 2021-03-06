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
public class SubscriptExpr extends Expr {

    private final Expr subscriptBase;
    private final Expr subspos;

    public SubscriptExpr(Expr subscriptBase, Expr subspos) {
        this.subscriptBase = subscriptBase;
        this.subspos = subspos;
    }

    @Override
    public String getText() {
        return String.format("%s->%s", subscriptBase.getText(), subspos.getText());
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
    
    
    @Override
    public Expr optimize() {
        final Expr subsBaseOpt = subscriptBase.optimize();
        final Expr subsPosOpt = subspos.optimize();

        if (subsBaseOpt instanceof ListExpr) {
            final ListExpr list = (ListExpr) subsBaseOpt;
            if (subsPosOpt instanceof IntExpr) {
                int index = ((IntExpr) subsPosOpt).toInt();
                if (index < list.getChildNodes() && index > -1) {
                    return list.getChildNode(index);
                } else throw new AssertionError("Attempt to subscript position of " + index);
            }
        }

        return new SubscriptExpr(subsBaseOpt, subsPosOpt);
    }
}
