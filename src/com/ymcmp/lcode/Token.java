/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author plankp
 */
public final class Token implements Serializable {

    private static final long serialVersionUID = 68903451L;

    public final String tokenName;

    public final String tokenText;

    public Token(String tokenName, String tokenText) {
        this.tokenName = tokenName;
        this.tokenText = tokenText;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Token) {
            return this.tokenName.equals(((Token) obj).tokenName)
                    && this.tokenText.equals(((Token) obj).tokenText);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.tokenName);
        hash = 59 * hash + Objects.hashCode(this.tokenText);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", tokenName, tokenText);
    }
}
