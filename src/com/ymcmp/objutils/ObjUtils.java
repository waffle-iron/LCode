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
package com.ymcmp.objutils;

/**
 *
 * @author plankp
 */
public interface ObjUtils {

    /**
     * Tests if the current chain equals to a certain value. A code like <code>
     * ObjUtils.apply(a).is(b).is(c)</code> test if <code>a</code> equals to
     * both <code>b</code> and <code>c</code>. If comparison with <code>b</code>
     * returns <code>false</code>, <code>c</code> will not be executed.
     *
     * @param b The value being compared to
     *
     * @return The current chained expression
     */
    public ObjUtils is(Object b);

    /**
     * Chains another expression on to the current expression. A code like
     * <code>ObjUtils.apply(a).is(b).or().is(c)</code> tests whether or not
     * <code>a</code> equals to <code>b</code> or equals to <code>c</code>.
     * If <code>a</code> equals to <code>b</code>, than the chain containing
     * <code>c</code> will not be executed.
     *
     * @return The newly chained expression
     */
    public ObjUtils or();

    /**
     * Checks to see if the chained condition evaluates to true or false. If the
     * expression is not chained, then values <code>null</code>,
     * <code>false</code>, and <code>0</code> evaluate to false.
     *
     * @return If expression is true or false
     */
    public boolean translate();

    public Object getBaseVal();

    public static ObjUtils apply(Object val) {
        return new ObjUtilsImpl(val);
    }
}

/**
 *
 * @author plankp
 */
class ObjUtilsImpl implements ObjUtils {

    private final Object a;

    private ObjUtils child;

    public ObjUtilsImpl(Object val) {
        this.a = val;
    }

    @Override
    public Object getBaseVal() {
        return a;
    }

    @Override
    public ObjUtils is(Object b) {
        ObjUtilsIs obj = new ObjUtilsIs(a, b);
        child = obj;
        return obj;
    }

    @Override
    public ObjUtils or() {
        return child = new ObjUtilsOr(this);
    }

    @Override
    public boolean translate() {
        if (child == null) {
            return isTruthy(a);
        }
        return child.translate();
    }

    public static boolean isTruthy(final Object val) {
        return !(val == null || val.equals(false) || val.equals(0));
    }
}
