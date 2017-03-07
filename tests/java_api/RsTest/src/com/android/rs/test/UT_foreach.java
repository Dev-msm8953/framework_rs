/*
 * Copyright (C) 2011-2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.rs.test;

import android.content.Context;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;

public class UT_foreach extends UnitTest {
    private Allocation A;

    protected UT_foreach(RSTestCore rstc, Context ctx) {
        super(rstc, "ForEach", ctx);
    }

    private void initializeGlobals(RenderScript RS, ScriptC_foreach s) {
        Type.Builder typeBuilder = new Type.Builder(RS, Element.I32(RS));
        int X = 5;
        int Y = 7;
        s.set_dimX(X);
        s.set_dimY(Y);
        typeBuilder.setX(X).setY(Y);
        A = Allocation.createTyped(RS, typeBuilder.create());
        s.set_aRaw(A);

        return;
    }

    public void run() {
        RenderScript pRS = RenderScript.create(mCtx);
        ScriptC_foreach s = new ScriptC_foreach(pRS);
        pRS.setMessageHandler(mRsMessage);
        initializeGlobals(pRS, s);
        s.forEach_root(A);
        s.invoke_verify_root();
        s.forEach_foo(A, A);
        s.invoke_verify_foo();
        s.invoke_foreach_test();
        pRS.finish();
        waitForMessage();
        A.getType().destroy();
        A.destroy();
        s.destroy();
        pRS.destroy();
    }
}
