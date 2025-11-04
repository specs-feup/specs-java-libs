/**
 * Copyright 2016 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.z3helper;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntSort;
import com.microsoft.z3.Params;
import com.microsoft.z3.RealSort;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;

public class ContextHolder {
    public int refs;
    private final Context context;

    public ContextHolder() {
        this.context = new Context();
        this.refs = 1;
    }

    public void addRef() {
        if (this.refs == 0) {
            throw new RuntimeException("Already disposed of Context");
        }

        ++this.refs;
    }

    public void removeRef() {
        --this.refs;

        if (this.refs < 0) {
            throw new RuntimeException();
        }
        if (this.refs == 0) {
            context.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.refs != 0) {
            System.err.println("Missing call to Z3ContextContainer.removeRef");
        }

        super.finalize();
    }

    public Solver mkSolver() {
        Solver solver = this.context.mkSolver();

        return solver;
    }

    public BoolExpr mkEq(Expr arg0, Expr arg1) {
        if (refs == 0) {
            throw new IllegalStateException("Using context after being disposed.");
        }

        BoolExpr expr = this.context.mkEq(arg0, arg1);

        return expr;
    }

    public BoolExpr mkNe(ArithExpr arg0, ArithExpr arg1) {
        return mkNot(mkEq(arg0, arg1));
    }

    public BoolExpr mkGt(ArithExpr arg0, ArithExpr arg1) {
        BoolExpr expr = this.context.mkGt(arg0, arg1);

        return expr;
    }

    public BoolExpr mkGe(ArithExpr arg0, ArithExpr arg1) {
        BoolExpr expr = this.context.mkGe(arg0, arg1);

        return expr;
    }

    public BoolExpr mkLt(ArithExpr arg0, ArithExpr arg1) {
        BoolExpr expr = this.context.mkLt(arg0, arg1);

        return expr;
    }

    public BoolExpr mkLe(ArithExpr arg0, ArithExpr arg1) {
        BoolExpr expr = this.context.mkLe(arg0, arg1);

        return expr;
    }

    public BoolExpr mkNot(BoolExpr expr) {
        BoolExpr not = this.context.mkNot(expr);

        return not;
    }

    public RealSort mkRealSort() {
        RealSort sort = this.context.mkRealSort();

        return sort;
    }

    public IntSort mkIntSort() {
        IntSort sort = this.context.mkIntSort();

        return sort;
    }

    public ArithExpr mkAdd(ArithExpr... args) {
        ArithExpr expr = this.context.mkAdd(args);

        return expr;
    }

    public ArithExpr mkSub(ArithExpr... args) {
        ArithExpr expr = this.context.mkSub(args);

        return expr;
    }

    public Expr mkMul(ArithExpr... args) {
        ArithExpr expr = this.context.mkMul(args);

        return expr;
    }

    public Expr mkConst(String name, Sort sort) {
        Expr expr = this.context.mkConst(name, sort);

        return expr;
    }

    public Expr mkNumeral(String value, Sort sort) {
        Expr expr = this.context.mkNumeral(value, sort);

        return expr;
    }

    public Expr mkNumeral(int value, Sort sort) {
        Expr expr = this.context.mkNumeral(value, sort);

        return expr;
    }

    public Expr mkITE(BoolExpr arg0, Expr arg1, Expr arg2) {
        Expr expr = this.context.mkITE(arg0, arg1, arg2);

        return expr;
    }

    public Params mkParams() {
        Params params = this.context.mkParams();

        return params;
    }

    public BoolExpr mkTrue() {
        BoolExpr x = this.context.mkTrue();

        return x;
    }

    public BoolExpr mkFalse() {
        BoolExpr x = this.context.mkFalse();

        return x;
    }

    public BoolExpr mkAnd(BoolExpr a, BoolExpr b) {
        BoolExpr x = this.context.mkAnd(a, b);

        return x;
    }

    public BoolExpr mkOr(BoolExpr a, BoolExpr b) {
        BoolExpr x = this.context.mkOr(a, b);

        return x;
    }

    public BoolExpr mkImplies(BoolExpr a, BoolExpr b) {
        BoolExpr x = this.context.mkImplies(a, b);

        return x;
    }

    @Override
    public String toString() {
        return context.toString();
    }
}
