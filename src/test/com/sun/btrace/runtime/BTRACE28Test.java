/*
 * Copyright 2008-2010 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.btrace.runtime;

import support.InstrumentorTestBase;
import org.junit.Test;

/**
 *
 * @author Jaroslav Bachorik
 */
public class BTRACE28Test extends InstrumentorTestBase {
    @Test
    public void bytecodeValidation() throws Exception {
        originalBC = loadTargetClass("issues/BTRACE28");
        transform("issues/BTRACE28");
        checkTransformation("LDC \"resources.issues.BTRACE28\"\nLDC \"<init>\"\n" +
                            "INVOKESTATIC resources/issues/BTRACE28.$btrace$traces$issues$BTRACE28$tracker (Ljava/lang/String;Ljava/lang/String;)V\n" +
                            "MAXSTACK = 2\nASTORE 5\nASTORE 6\nASTORE 7\nALOAD 7\nASTORE 8\nALOAD 8\n" +
                            "LDC \"resources.issues.BTRACE28\"\nLDC \"serveResource\"\n" +
                            "INVOKESTATIC resources/issues/BTRACE28.$btrace$traces$issues$BTRACE28$tracker (Ljava/lang/String;Ljava/lang/String;)V");
    }
}
