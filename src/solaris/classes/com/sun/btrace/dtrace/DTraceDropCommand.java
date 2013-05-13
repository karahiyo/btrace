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

package com.sun.btrace.dtrace;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import org.opensolaris.os.dtrace.DropEvent;
import com.sun.btrace.comm.MessageCommand;

/**
 * Command class that represents DTrace drop event.
 *
 * @author A. Sundararajan
 */
public class DTraceDropCommand extends MessageCommand 
             implements DTraceCommand {
    private DropEvent de;
    public DTraceDropCommand(DropEvent de) {
        super(asString(de));
        this.de = de;
    }

    /**
     * Returns the underlying DTrace drop event
     */
    public DropEvent getDropEvent() {
        return de;
    }

    public void write(ObjectOutput out) throws IOException {
        super.write(out);
        out.writeObject(out);
    }

    public void read(ObjectInput in) 
                throws ClassNotFoundException, IOException {
        super.read(in);
        de = (DropEvent) in.readObject();
    }

    private static String asString(DropEvent de) {
        return de.getDrop().getDefaultMessage();
    }
}

