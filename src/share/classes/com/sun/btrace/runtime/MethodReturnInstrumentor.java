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

import com.sun.btrace.org.objectweb.asm.Type;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.sun.btrace.org.objectweb.asm.ClassReader;
import com.sun.btrace.org.objectweb.asm.ClassVisitor;
import com.sun.btrace.org.objectweb.asm.ClassWriter;
import com.sun.btrace.org.objectweb.asm.MethodVisitor;
import com.sun.btrace.org.objectweb.asm.Opcodes;
import static com.sun.btrace.org.objectweb.asm.Opcodes.*;

/**
 * This visitor helps in inserting code whenever a method 
 * call returns. The code to insert on method return may be 
 * decided by derived class. By default, this class inserts 
 * code to print name and signature of the method returned.
 *
 * @author A. Sundararajan
 */
public class MethodReturnInstrumentor extends MethodInstrumentor {
    public MethodReturnInstrumentor(MethodVisitor mv, int[] tsIndex, String parentClz, String superClz,
        int access, String name, String desc) {
        super(mv, parentClz, superClz, access, name, desc);
    }


    public void visitInsn(int opcode) {
        switch (opcode) {
            case IRETURN:
            case ARETURN:
            case FRETURN:                           
            case LRETURN:
            case DRETURN:
            case RETURN:
                onMethodReturn(opcode);
                break;
            default:                           
                break;
        }
        super.visitInsn(opcode);
    }

    protected void loadReturnParameter(int opcode) {
        super.dupReturnValue(opcode);
    }

    protected void onMethodReturn(int opcode) {
        println("leaving " + getName() + getDescriptor());
    }

    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java com.sun.btrace.runtime.MethodReturnInstrumentor <class>");
            System.exit(1);
        }

        args[0] = args[0].replace('.', '/');
        FileInputStream fis = new FileInputStream(args[0] + ".class");
        ClassReader reader = new ClassReader(new BufferedInputStream(fis));
        FileOutputStream fos = new FileOutputStream(args[0] + ".class");
        ClassWriter writer = InstrumentUtils.newClassWriter();
        InstrumentUtils.accept(reader, 
            new ClassVisitor(Opcodes.ASM4, writer) {
                 public MethodVisitor visitMethod(int access, String name, String desc, 
                     String signature, String[] exceptions) {
                     MethodVisitor mv = super.visitMethod(access, name, desc, 
                             signature, exceptions);
                     return new MethodReturnInstrumentor(mv, null, args[0], args[0], access, name, desc);
                 }
            });
        fos.write(writer.toByteArray());
    }

    protected void loadDurationParameter(int localVarIndex1, int localVarIndex2) {
        if (localVarIndex1 != -1 && localVarIndex2 != -1) {
            super.loadLocal(Type.LONG_TYPE, localVarIndex2);
            super.loadLocal(Type.LONG_TYPE, localVarIndex1);
            super.visitInsn(LSUB);
        }
    }
}