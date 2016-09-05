/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode;

import com.ymcmp.lcode.ast.Expr;
import com.ymcmp.lcode.objutils.ObjUtils;
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
        List<Token> apply = new LCodeLexer("{&a : 10 &b : 1.7 &c : [a : a](6) &d : {} &e : '()}").apply();
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
