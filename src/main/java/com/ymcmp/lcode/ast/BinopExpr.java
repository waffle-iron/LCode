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
package com.ymcmp.lcode.ast;

/**
 *
 * @author plankp
 */
public class BinopExpr extends Expr {

    private final Expr left, right;
    private final Expr op;

    public BinopExpr(Expr left, String op, Expr right) {
        this.left = left;
        this.op = new OpExpr(op);
        this.right = right;
    }
    
    private BinopExpr(Expr left, Expr op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public String getText() {
        return String.format("%s%s%s", left.getText(), op.getText(), right.getText());
    }

    @Override
    public int getChildNodes() {
        return 3;
    }

    @Override
    public Expr getChildNode(int nodeIndex) {
        switch (nodeIndex) {
        case 0:
            return left;
        case 1:
            return op;
        case 2:
            return right;
        default:
            return EmptyExpr.getInstance();
        }
    }

    @Override
    public String toTree() {
        return String.format("(%s %s %s)", op.toTree(), left.toTree(), right.toTree());
    }

    @Override
    public Expr optimize() {
        final Expr leftOpt = left.optimize();
        final Expr rightOpt = right.optimize();

        switch (op.getText()) {
            case "+":
                if (leftOpt instanceof IntExpr) {
                    final IntExpr leftInt = (IntExpr) leftOpt;
                    if (leftInt.toInt() == 0) {
                        return rightOpt;
                    }
                    if (rightOpt instanceof IntExpr) {
                        final IntExpr rightInt = (IntExpr) rightOpt;
                        return new IntExpr(leftInt.toInt() + rightInt.toInt());
                    }
                    if (rightOpt instanceof FloatExpr) {
                        final FloatExpr rightFloat = (FloatExpr) rightOpt;
                        return new FloatExpr(leftInt.toInt() + rightFloat.toDouble());
                    }
                }
                if (leftOpt instanceof FloatExpr) {
                    final FloatExpr leftFloat = (FloatExpr) leftOpt;
                    if (rightOpt instanceof NumberExpr) {
                        return new FloatExpr(leftFloat.toDouble() + ((NumberExpr) rightOpt).toDouble());
                    }
                }
                if (rightOpt instanceof IntExpr) {
                    final IntExpr rightInt = (IntExpr) rightOpt;
                    if (rightInt.toInt() == 0) {
                        return leftOpt;
                    }
                }
                break;
            case "-":
                if (leftOpt instanceof IntExpr) {
                    final IntExpr leftInt = (IntExpr) leftOpt;
                    if (rightOpt instanceof IntExpr) {
                        return new IntExpr(leftInt.toInt() - ((IntExpr) rightOpt).toInt());
                    }
                    if (rightOpt instanceof FloatExpr) {
                        return new FloatExpr(leftInt.toInt() - ((FloatExpr) rightOpt).toDouble());
                    }
                }
                if (leftOpt instanceof FloatExpr) {
                    final FloatExpr leftFloat = (FloatExpr) leftOpt;
                    if (rightOpt instanceof NumberExpr) {
                        return new FloatExpr(leftFloat.toDouble() - ((NumberExpr) rightOpt).toDouble());
                    }
                }
                if (rightOpt instanceof IntExpr) {
                    final IntExpr rightInt = (IntExpr) rightOpt;
                    if (rightInt.toInt() == 0) {
                        return leftOpt;
                    }
                }
                if ((leftOpt instanceof IndentExpr) && (rightOpt instanceof IndentExpr)) {
                    final IndentExpr leftInd = (IndentExpr) leftOpt;
                    final IndentExpr rightInd = (IndentExpr) rightOpt;
                    if (leftInd.getText().equals(rightInd.getText())) {
                        return new IntExpr(0);
                    }
                }
                break;
            case "*":
                if (leftOpt instanceof IntExpr) {
                    final IntExpr leftInt = (IntExpr) leftOpt;
                    if (leftInt.toInt() == 0) {
                        return leftInt;
                    }
                    if (rightOpt instanceof IntExpr) {
                        return new IntExpr(leftInt.toInt() * ((IntExpr) rightOpt).toInt());
                    }
                    if (rightOpt instanceof FloatExpr) {
                        return new FloatExpr(leftInt.toInt() * ((FloatExpr) rightOpt).toDouble());
                    }
                }
                if (leftOpt instanceof FloatExpr) {
                    final FloatExpr leftFloat = (FloatExpr) leftOpt;
                    if (leftFloat.toDouble() == 0) {
                        return leftFloat;
                    }
                    if (rightOpt instanceof NumberExpr) {
                        return new FloatExpr(leftFloat.toDouble() * ((NumberExpr) rightOpt).toDouble());
                    }
                }
                if (rightOpt instanceof IntExpr) {
                    final IntExpr rightInt = (IntExpr) rightOpt;
                    switch (rightInt.toInt()) {
                        case 0: return rightInt;
                        case 1: return leftOpt;
                    }
                }
                break;
            case "/":
                // Must check for divide by zero before everything else
                if (rightOpt instanceof IntExpr) {
                    final IntExpr rightInt = (IntExpr) rightOpt;
                    switch (rightInt.toInt()) {
                        case 0: throw new AssertionError("Dividing by zero!");
                        case 1: return leftOpt;
                    }
                }
                if (leftOpt instanceof IntExpr) {
                    final IntExpr leftInt = (IntExpr) leftOpt;
                    if (rightOpt instanceof IntExpr) {
                        return new IntExpr(leftInt.toInt() / ((IntExpr) rightOpt).toInt());
                    }
                    if (rightOpt instanceof FloatExpr) {
                        return new FloatExpr(leftInt.toInt() / ((FloatExpr) rightOpt).toDouble());
                    }
                    if (leftInt.toInt() == 0) {
                        return leftInt;
                    }
                }
                if (leftOpt instanceof FloatExpr) {
                    final FloatExpr leftFloat = (FloatExpr) leftOpt;
                    if (rightOpt instanceof NumberExpr) {
                        return new FloatExpr(leftFloat.toDouble() / ((NumberExpr) rightOpt).toDouble());
                    }
                    if (leftFloat.toDouble() == 0) {
                        return leftFloat;
                    }
                }
                if ((leftOpt instanceof IndentExpr) && (rightOpt instanceof IndentExpr)) {
                    final IndentExpr leftInd = (IndentExpr) leftOpt;
                    final IndentExpr rightInd = (IndentExpr) rightOpt;
                    if (leftInd.getText().equals(rightInd.getText())) {
                        return new IntExpr(1);
                    }
                }
                break;
        }
        return new BinopExpr(leftOpt, op, rightOpt);
    }
}
