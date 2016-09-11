/*
 * The MIT License
 *
 * Copyright 2016 plankp.
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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author plankp
 */
public class ObjUtilsTest {

    public ObjUtilsTest() {
    }

    /**
     * Test of is method, of class ObjUtils.
     */
    @Test
    public void testIs() {
        assertTrue(ObjUtils.apply(10).is(10).translate());
        assertFalse(ObjUtils.apply(10).is(10).is(20).translate());
    }

    @Test
    public void testJsOr() {
        // Testing for javascript like syntax `expr1 || expr2`
        assertTrue(ObjUtils.apply(true).or().is(false).translate());
        assertTrue(ObjUtils.apply(false).or().is(true).translate());
        assertTrue(ObjUtils.apply(0).or().is(10).translate());
        assertTrue(ObjUtils.apply(1).or().is(2).translate());
    }

    @Test
    public void testOr() {
        assertTrue(ObjUtils.apply(1).is(1).or().is(2).translate());
        assertTrue(ObjUtils.apply("a").is(1).or().is("a").translate());
        assertTrue(ObjUtils.apply(null).is(null).or().is(null).translate());
    }

    @Test
    public void testTranslateTrue() {
        assertTrue(ObjUtils.apply(true).translate());
        assertTrue(ObjUtils.apply(1).translate());
        assertTrue(ObjUtils.apply("Abc").translate());
    }

    @Test
    public void testTranslateFalse() {
        assertFalse(ObjUtils.apply(false).translate());
        assertFalse(ObjUtils.apply(0).translate());
        assertFalse(ObjUtils.apply(null).translate());
    }

    @Test
    public void testGetBaseVal() {
        final ObjUtils wrap = ObjUtils.apply("Abc");
        assertEquals("Abc", wrap.getBaseVal());
    }

}
