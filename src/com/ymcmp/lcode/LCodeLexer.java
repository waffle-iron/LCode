/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ymcmp.lcode;

import java.util.ArrayList;
import java.util.List;

/**
 * The list of tokens:
 * <p>
 * _ = [ \t\r\n]* <br>
 *   | '#|' (![\r\n]* [\r\n] '|')* ![\r\n]* <br>
 *   | '#' ![\r\n]* <br>
 * BOOL (ident) = 'true' | 'false' <br>
 * SYM (ident) = '$' [a-zA-Z0-9$_]* [?!]? <br>
 * IDENT = [a-zA-Z][a-zA-Z0-9$_]* [?!]? <br>
 * NUM_I = ('0' | [1-9][0-9]*) <br>
 * NUM_F = ('0' | [1-9][0-9]*) '.' [0-9]+ <br>
 * PAREN_O = '(' <br>
 * PAREN_C = ')' <br>
 * SQUARE_O = '[' <br>
 * SQUARE_C = ']' <br>
 * BRACE_O = '{' <br>
 * BRACE_C = '}' <br>
 * QUOTE = [`'] <br>
 * USCRL = '_' <br>
 * COLON = ':' <br>
 * ARROW = '-&gt;' <br>
 * MULOP = '*' <br>
 * DIVOP = '/' <br>
 * MODOP = '%' <br>
 * ADDOP = '+' <br>
 * SUBOP (arrow) = '-' <br>
 *
 * @author plankp
 */
public class LCodeLexer {

    public final String source;

    public LCodeLexer(String src) {
        source = src;
    }

    public List<Token> apply() {
        final List<Token> tokens = new ArrayList<>();
        final char[] arr = source.toCharArray();
        final int arr_len = arr.length;

        for (int i = 0; i < arr_len; i++) {
            final char current = arr[i];
            if (isLCodeWhitespace(current)) {
                StringBuilder buf = new StringBuilder();
                buf.append(current);
                for (i++; i < arr_len; i++) {
                    if (!isLCodeWhitespace(arr[i])) {
                        break;
                    }
                    buf.append(arr[i]);
                }
                i--;
                tokens.add(new Token("_", buf.toString()));
            } else if (current == '#') {
                StringBuilder buf = new StringBuilder();
                buf.append(current);
                for (i++; i < arr_len; i++) {
                    char c = arr[i];
                    if (c == '\r' || c == '\n') {
                        break;
                    }
                    buf.append(c);
                }
                i--;
                tokens.add(new Token("_", buf.toString()));
            } else if (isLCodeQuote(current)) {
                tokens.add(new Token("QUOTE", Character.toString(current)));
            } else if (current == '-') {
                if (++i < arr_len) {
                    if (arr[i] == '>') {
                        tokens.add(new Token("ARROW", "->"));
                        continue;
                    }
                }
                tokens.add(new Token("SUBOP", Character.toString(current)));
            } else if (current == '+') {
                tokens.add(new Token("ADDOP", Character.toString(current)));
            } else if (current == '_') {
                tokens.add(new Token("USCRL", Character.toString(current)));
            } else if (current == '*') {
                tokens.add(new Token("MULOP", Character.toString(current)));
            } else if (current == '/') {
                tokens.add(new Token("DIVOP", Character.toString(current)));
            } else if (current == '%') {
                tokens.add(new Token("MODOP", Character.toString(current)));
            } else if (current == ':') {
                tokens.add(new Token("COLON", Character.toString(current)));
            } else if (current == '{') {
                tokens.add(new Token("BRACE_O", Character.toString(current)));
            } else if (current == '}') {
                tokens.add(new Token("BRACE_C", Character.toString(current)));
            } else if (current == '[') {
                tokens.add(new Token("SQUARE_O", Character.toString(current)));
            } else if (current == ']') {
                tokens.add(new Token("SQUARE_C", Character.toString(current)));
            } else if (current == '(') {
                tokens.add(new Token("PAREN_O", Character.toString(current)));
            } else if (current == ')') {
                tokens.add(new Token("PAREN_C", Character.toString(current)));
            } else if (isLCodeDigit(current)) {
                StringBuilder buf = new StringBuilder();
                buf.append(current);
                if (current != '0') {
                    for (i++; i < arr_len; i++) {
                        if (!isLCodeDigit(arr[i])) {
                            break;
                        }
                        buf.append(arr[i]);
                    }
                    i--;
                }
                if (i + 1 < arr_len) {
                    if (arr[i + 1] == '.') {
                        buf.append(arr[++i]);
                        for (i++; i < arr_len; i++) {
                            if (!isLCodeDigit(arr[i])) {
                                break;
                            }
                            buf.append(arr[i]);
                        }
                        i--;
                        final String tmpFloat = buf.toString();
                        if (tmpFloat.charAt(tmpFloat.length() - 1) == '.') {
                            throw new AssertionError("Illegal number '" + tmpFloat + "'. Maybe you forgot a zero?");
                        }
                        tokens.add(new Token("NUM_F", tmpFloat));
                        continue;
                    }
                }
                tokens.add(new Token("NUM_I", buf.toString()));
            } else if (isLCodeIdentFirstChar(current)) {
                StringBuilder buf = new StringBuilder();
                buf.append(current);
                for (i++; i < arr_len; i++) {
                    if (!isLCodeIdent(arr[i])) {
                        if (isLCodeIdentLastChar(arr[i])) {
                            buf.append(arr[i]);
                            i++; // Increment position
                        }
                        break;
                    }
                    buf.append(arr[i]);
                }
                i--;
                final String tmpIdent = buf.toString();
                switch (tmpIdent) {
                case "true":
                case "false":
                    tokens.add(new Token("BOOL", tmpIdent));
                    break;
                default:
                    if (tmpIdent.charAt(0) == '$') {
                        tokens.add(new Token("SYM", tmpIdent));
                    } else {
                        tokens.add(new Token("IDENT", tmpIdent));
                    }
                }
            } else {
                throw new AssertionError("Unexpected text from source: " + source.substring(i));
            }
        }

        return tokens;
    }

    private boolean isLCodeWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private boolean isLCodeQuote(char c) {
        return c == '\'' || c == '`';
    }

    private boolean isLCodeDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isLCodeIdentFirstChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '$';
    }

    private boolean isLCodeIdent(char c) {
        return isLCodeIdentFirstChar(c) || isLCodeDigit(c) || c == '$' || c == '_';
    }

    private boolean isLCodeIdentLastChar(char c) {
        return isLCodeIdent(c) || c == '?' || c == '!';
    }
}
