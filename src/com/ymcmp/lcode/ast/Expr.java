/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode.ast;

import java.io.Serializable;

/**
 *
 * @author plankp
 */
public abstract class Expr implements Serializable {

    private static final long serialVersionUID = 807819856L;

    public static Expr[] EMPTY_EXPR_LIST = new Expr[0];

    protected String text;

    public final String getText() {
        return text;
    }

    public abstract int getChildNodes();

    public abstract Expr getChildNode(int nodeIndex);

    public String toTree() {
        return getText();
    }
}
