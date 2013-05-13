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
 * This visitor helps in inserting code whenever an field access
 * is done. The code to insert on field access may be decided by 
 * derived class. By default, this class inserts code to print 
 * the field access.
 *
 * @author A. Sundararajan
 */
public class FieldAccessInstrumentor extends MethodInstrumentor {
    protected boolean isStaticAccess = false;

    public FieldAccessInstrumentor(MethodVisitor mv, String parentClz, String superClz,
        int access, String name, String desc) {
        super(mv, parentClz, superClz, access, name, desc);
    }

    public void visitFieldInsn(int opcode, String owner, 
        String name, String desc) {
        boolean get;
        if (opcode == GETFIELD || opcode == GETSTATIC) {
            get = true;
        } else {
            get = false;
        }
        isStaticAccess = (opcode == GETSTATIC || opcode == PUTSTATIC);

        if (get) {
            onBeforeGetField(opcode, owner, name, desc);
        } else {
            onBeforePutField(opcode, owner, name, desc);
        }
        super.visitFieldInsn(opcode, owner, name, desc);
        if (get) {
            onAfterGetField(opcode, owner, name, desc);
        } else {
            onAfterPutField(opcode, owner, name, desc);
        }
    }

    protected void onBeforeGetField(int opcode,
        String owner, String name, String desc) {}

    protected void onAfterGetField(int opcode,
        String owner, String name, String desc) {}

    protected void onBeforePutField(int opcode,
        String owner, String name, String desc) {}

    protected void onAfterPutField(int opcode,
        String owner, String name, String desc) {}

    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java com.sun.btrace.runtime.FieldAccessInstrumentor <class>");
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
                     return new FieldAccessInstrumentor(mv, args[0], args[0], access, name, desc) {

                        @Override
                        protected void onAfterGetField(int opcode, String owner, String name, String desc) {
                            println("after get: " + owner + "." + name);
                        }

                        @Override
                        protected void onAfterPutField(int opcode, String owner, String name, String desc) {
                            println("after put: " + owner + "." + name);
                        }

                        @Override
                        protected void onBeforeGetField(int opcode, String owner, String name, String desc) {
                            println("before get: " + owner + "." + name);
                        }

                        @Override
                        protected void onBeforePutField(int opcode, String owner, String name, String desc) {
                            println("before put: " + owner + "." + name);
                        }

                     };
                 }
            });
        fos.write(writer.toByteArray());
    }
}