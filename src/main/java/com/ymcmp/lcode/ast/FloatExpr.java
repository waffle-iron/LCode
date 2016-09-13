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
public class FloatExpr extends NumberExpr {

    private final String text;

    public FloatExpr(String val) {
        this.text = val;
    }

    public FloatExpr(double val) {
        String s = Double.toString(val);
        if (!(Double.isNaN(val) || Double.isInfinite(val))) {
            if (!s.contains(".")) {
                s = s.concat(".0");
            }
        }
        text = s;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getChildNodes() {
        return 1;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        return nodeIndex == 0 ? this : EmptyExpr.getInstance();
    }

    @Override
    public int toInt() {
        return (int) toDouble();
    }

    @Override
    public double toDouble() {
        return Double.parseDouble(text);
    }
}
