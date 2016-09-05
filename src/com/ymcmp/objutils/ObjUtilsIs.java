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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author plankp
 */
class ObjUtilsIs implements ObjUtils {

    private final List<Object> cmpList = new ArrayList<>();

    private ObjUtils parentNode;

    public ObjUtilsIs(Object a, Object b) {
        this(null, a, b);
    }

    public ObjUtilsIs(ObjUtils parent, Object a, Object b) {
        parentNode = parent;
        this.cmpList.add(a);
        this.cmpList.add(b);
    }

    @Override
    public ObjUtils is(Object b) {
        this.cmpList.add(b);
        return this;
    }

    @Override
    public ObjUtils or() {
        return new ObjUtilsOr(this);
    }

    @Override
    public Object getBaseVal() {
        return cmpList.get(0);
    }

    @Override
    public boolean translate() {
        if (parentNode != null) {
            // Used for conditions like Or
            final ObjUtils tmp = parentNode;
            parentNode = null; // parent *translate* this will call the other part
            final boolean ret = tmp.translate();
            parentNode = tmp; // Restore old parentNode
            return ret;
        }
        boolean ret = true;
        final Object first = cmpList.get(0);
        final int size = cmpList.size();
        for (int i = 1; i < size; i++) {
            if (!ret) {
                break;
            }
            ret = Objects.equals(first, cmpList.get(i));
        }
        return ret;
    }

}
