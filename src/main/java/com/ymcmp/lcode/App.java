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
package com.ymcmp.lcode;

import com.ymcmp.lcode.ast.Expr;
import java.util.List;

/**
 *
 * @author plankp
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<Token> apply = new LCodeLexer(""
                + "#| A map...\n"
                + "| And stuff..\n"
                + "{$a : 10 $b : 1.7 $c : [a : a](6) $d : {} $e : '()}").apply();
        LCodeParser parser = new LCodeParser(apply);
        System.out.println("Raw tokens:");
        parser.tokens.forEach(System.out::print);
        System.out.println("\nRestoration:");
        final Expr ast = parser.apply();
        System.out.println(ast.getText());
        System.out.println("Tree:");
        System.out.println(ast.toTree());
    }
}
