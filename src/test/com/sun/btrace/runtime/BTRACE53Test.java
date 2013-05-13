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
public class BTRACE53Test extends InstrumentorTestBase {
    @Test
    public void bytecodeValidation() throws Exception {
        originalBC = loadTargetClass("DerivedClass");
        transform("issues/BTRACE53");
        checkTransformation("INVOKESTATIC resources/DerivedClass.$btrace$time$stamp ()J\nLSTORE 1\n"
                          + "INVOKESTATIC resources/DerivedClass.$btrace$time$stamp ()J\nLSTORE 3\n"
                          + "LLOAD 3\nLLOAD 1\nLSUB\nINVOKESTATIC resources/DerivedClass.$btrace$traces$issues$BTRACE53$onInit (J)V\n"
                          + "MAXSTACK = 4\nMAXLOCALS = 5");
    }
}
