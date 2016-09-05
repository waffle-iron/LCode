/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode;

import com.ymcmp.lcode.ast.BinopExpr;
import com.ymcmp.lcode.ast.BoolExpr;
import com.ymcmp.lcode.ast.CallExpr;
import com.ymcmp.lcode.ast.Expr;
import com.ymcmp.lcode.ast.FloatExpr;
import com.ymcmp.lcode.ast.FuncExpr;
import com.ymcmp.lcode.ast.IndentExpr;
import com.ymcmp.lcode.ast.IntExpr;
import com.ymcmp.lcode.ast.ListExpr;
import com.ymcmp.lcode.ast.SubscriptExpr;
import com.ymcmp.lcode.ast.SymbolExpr;
import com.ymcmp.lcode.ast.MapExpr;
import com.ymcmp.lcode.objutils.ObjUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author plankp
 */
public class LCodeParser {

    private static class IntRef {

        public int value;

        public IntRef() {
        }

        public IntRef(int value) {
            this.value = value;
        }
    }

    public final List<Token> tokens;

    public LCodeParser(List<Token> tokens) {
        this.tokens = tokens.stream().filter(el -> !el.tokenName.equals("_"))
                .collect(Collectors.toList());
    }

    public Expr apply() {
        IntRef ref = new IntRef();
        return consExpr(ref);
    }

    /*
     * consExpr = consAddLike
     */
    private Expr consExpr(IntRef index) {
        return consAddLike(index);
    }

    /*
     * consAddLike = consMulLike ((ADDOP | SUBOP) consMulLike)*
     */
    private Expr consAddLike(IntRef index) {
        Expr head = consMulLike(index);
outer:  while (index.value < tokens.size()) {
            final Token tok = tokens.get(index.value++);
            switch (tok.tokenName) {
            case "ADDOP":
            case "SUBOP":
                head = new BinopExpr(head, tok.tokenText, consMulLike(index));
                break;
            default:
                index.value--;
                break outer;
            }
        }
        return head;
    }

    /*
     * consMulLike = consPostFix ((MULOP | DIVOP | MODOP) consPostFix)*
     */
    private Expr consMulLike(IntRef index) {
        Expr head = consPostFix(index);
outer:  while (index.value < tokens.size()) {
            final Token tok = tokens.get(index.value++);
            switch (tok.tokenName) {
            case "MULOP":
            case "DIVOP":
            case "MODOP":
                head = new BinopExpr(head, tok.tokenText, consPostFix(index));
                break;
            default:
                index.value--;
                break outer;
            }
        }
        return head;
    }

    /*
     * consPostFix = consMapLit consPostFixTail*
     */
    private Expr consPostFix(IntRef index) {
        return consPostFixTail(consMapLit(index), index);
    }

    /*
     * (listLit)
     * consPostFixTail = PAREN_O consExpr* PAREN_C
     *                 | ARROW consExpr (COLON consExpr)?
     */
    private Expr consPostFixTail(Expr listLit, IntRef index) {
outer:  while (index.value < tokens.size()) {
            final Token tok = tokens.get(index.value++);
            switch (tok.tokenName) {
            case "PAREN_O": {
                final List<Expr> tmp = subBraced(index, "Unclosed argument list");
                listLit = new CallExpr(listLit, tmp.toArray(new Expr[tmp.size()]));
                break;
            }
            case "ARROW": {
                final Expr subpos = consExpr(index);
                listLit = new SubscriptExpr(listLit, subpos);
                break;
            }
            default:
                // Note: default just returns the listLit, does not give error
                index.value--;
                break outer;
            }
        }
        return listLit;
    }

    /*
     * consMapLit = BRACE_O (consExpr COLON consExpr)* BRACE_C
     *            | consListLit
     */
    private Expr consMapLit(IntRef index) {
        if (index.value < tokens.size()) {
            final Token tok = tokens.get(index.value++);
            switch (tok.tokenName) {
            case "BRACE_O": {
                if (index.value < tokens.size()) {
                    final List<MapExpr.ExprPair> nodes = new ArrayList<>();
                    Token next = tokens.get(index.value++);
                    while(!next.tokenName.equals("BRACE_C")) {
                        index.value--;
                        nodes.add(consMapPair(index));
                        next = tokens.get(index.value++);
}
                    return new MapExpr(nodes.toArray(new MapExpr.ExprPair[nodes.size()]));
                }
                throw new AssertionError("Unclosed map literal");
            }
            default:
                index.value--;
                return consListLit(index);
            }
        }
        throw new AssertionError("Needs map composition literal or list composition literal");
    }

    /*
     * consMapPair = consExpr COLON consExpr
     */
    private MapExpr.ExprPair consMapPair(IntRef index) {
        if (index.value < tokens.size()) {
            final Expr key = consExpr(index);
            if (index.value < tokens.size()) {
                final Token tok = tokens.get(index.value++);
                if (tok.tokenName.equals("COLON")) {
                    return new MapExpr.ExprPair(key, consExpr(index));
                }
            }
            throw new AssertionError("Requires a colon");
        }
        throw new AssertionError("Needs a pair");
    }

    /*
     * consListLit = QUOTE PAREN_O consExpr* PAREN_C
     *             | QUOTE consExpr
     *             | consBasic
     */
    private Expr consListLit(IntRef index) {
        if (index.value < tokens.size()) {
            final Token tok = tokens.get(index.value++);
            switch (tok.tokenName) {
            case "QUOTE": {
                if (index.value < tokens.size()) {
                    Token lookahead = tokens.get(index.value++);
                    switch (lookahead.tokenName) {
                    case "PAREN_O": {
                        final List<Expr> tmp = subBraced(index, "Unclosed list pairing");
                        return new ListExpr(tmp.toArray(new Expr[tmp.size()]));
                    }
                    default:
                        index.value--;
                        return new ListExpr(consExpr(index));
                    }
                }
                throw new AssertionError("Needs list pairing or list literal");
            }
            default:
                index.value--;
                return consBasic(index);
            }
        }
        throw new AssertionError("Needed list composition literal or basic value");
    }

    private List<Expr> subBraced(IntRef index, String failMsg) {
        try {
            final List<Expr> tmp = new ArrayList<>();
            while (!tokens.get(index.value++).tokenName.equals("PAREN_C")) {
                index.value--;
                tmp.add(consExpr(index));
            }
            return tmp;
        } catch (IndexOutOfBoundsException ex) {
            throw new AssertionError(failMsg);
        }
    }

    /*
     * consBasic = NUM_I | NUM_F | BOOL | IDENT | consFunc
     */
    private Expr consBasic(IntRef index) {
        if (index.value < tokens.size()) {
            final Token tok = tokens.get(index.value++);
            switch (tok.tokenName) {
            case "NUM_I":
                return new IntExpr(tok.tokenText);
            case "NUM_F":
                return new FloatExpr(tok.tokenText);
            case "BOOL":
                return new BoolExpr(tok.tokenText);
            case "IDENT":
                return new IndentExpr(tok.tokenText);
            case "SYM":
                return new SymbolExpr(tok.tokenText);
            case "SQUARE_O":
                index.value--;
                return consFunc(index);
            }
        }
        throw new AssertionError("Needed primitives such as Int, Float, Bool, Lambda Expression, Identifier, or a Symbol");
    }

    /*
     * consFunc = SQUARE_O (IDENT | USCRL)* COLON consExpr SQUARE_C
     *          | SQUARE_O consExpr SQUARE_C
     */
    private Expr consFunc(IntRef index) {
        if (index.value < tokens.size()) {
            final Token tok = tokens.get(index.value++);
            if (tok.tokenName.equals("SQUARE_O")) {
                final List<IndentExpr> params = new ArrayList<>();
                final Expr ret;
                Token maybeId = tokens.get(index.value++);
                switch (maybeId.tokenName) {
                case "IDENT":
                case "USCRL":
                    do {
                        params.add(new IndentExpr(maybeId.tokenText, maybeId.tokenText.equals("_")));
                    } while (ObjUtils.apply((maybeId = tokens.get(index.value++)).tokenName)
                            .is("IDENT").or().is("USCRL")
                            .translate()); // fallthrough
                    if (maybeId.tokenName.equals("COLON")) {
                        index.value++;
                    } else {
                        throw new AssertionError("Unclosed parameter list in Lambda Expression ");
                    }
                default:
                    index.value--; // fallthrough
                case "COLON":
                    ret = new FuncExpr(params.toArray(new IndentExpr[params.size()]), consExpr(index));
                }
                if ((index.value >= tokens.size())
                        || !tokens.get(index.value++).tokenName.equals("SQUARE_C")) {
                    throw new AssertionError("Unclosed Lambda Expression");
                }
                return ret;
            }
        }
        throw new AssertionError("Needs a Lambda Expression");
    }
}
