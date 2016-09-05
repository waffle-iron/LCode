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
package com.ymcmp.lcode.objutils;

/**
 *
 * @author plankp
 */
public interface ObjUtils {

    public ObjUtils is(Object b);

    public ObjUtils or();

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
class ObjUtilsTerminalTrue implements ObjUtils {

    private ObjUtilsIs pat;

    @Override
    public ObjUtils is(Object b) {
        return pat = new ObjUtilsIs(true, b);
    }

    @Override
    public ObjUtils or() {
        return this;
    }

    @Override
    public boolean translate() {
        if (pat == null) {
            return true;
        }
        return pat.translate();
    }

    @Override
    public Object getBaseVal() {
        return true;
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
        if (child == null) {
            return new ObjUtilsTerminalTrue();
        }
        return child = new ObjUtilsOr(this);
    }

    @Override
    public boolean translate() {
        return child.translate();
    }
}
