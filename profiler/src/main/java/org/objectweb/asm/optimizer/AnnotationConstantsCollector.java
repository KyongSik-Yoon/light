/*
 * Copyright 2015 GeekSaga.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.objectweb.asm.optimizer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * An {@link AnnotationVisitor} that collects the {@link Constant}s of the
 * annotations it visits.
 * 
 * @author Eric Bruneton
 */
public class AnnotationConstantsCollector extends AnnotationVisitor {

    private final ConstantPool cp;

    public AnnotationConstantsCollector(final AnnotationVisitor av,
            final ConstantPool cp) {
        super(Opcodes.ASM5, av);
        this.cp = cp;
    }

    @Override
    public void visit(final String name, final Object value) {
        if (name != null) {
            cp.newUTF8(name);
        }
        if (value instanceof Byte) {
            cp.newInteger(((Byte) value).byteValue());
        } else if (value instanceof Boolean) {
            cp.newInteger(((Boolean) value).booleanValue() ? 1 : 0);
        } else if (value instanceof Character) {
            cp.newInteger(((Character) value).charValue());
        } else if (value instanceof Short) {
            cp.newInteger(((Short) value).shortValue());
        } else if (value instanceof Type) {
            cp.newUTF8(((Type) value).getDescriptor());
        } else if (value instanceof byte[]) {
            byte[] v = (byte[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof boolean[]) {
            boolean[] v = (boolean[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i] ? 1 : 0);
            }
        } else if (value instanceof short[]) {
            short[] v = (short[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof char[]) {
            char[] v = (char[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof int[]) {
            int[] v = (int[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof long[]) {
            long[] v = (long[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newLong(v[i]);
            }
        } else if (value instanceof float[]) {
            float[] v = (float[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newFloat(v[i]);
            }
        } else if (value instanceof double[]) {
            double[] v = (double[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newDouble(v[i]);
            }
        } else {
            cp.newConst(value);
        }
        av.visit(name, value);
    }

    @Override
    public void visitEnum(final String name, final String desc,
                          final String value) {
        if (name != null) {
            cp.newUTF8(name);
        }
        cp.newUTF8(desc);
        cp.newUTF8(value);
        av.visitEnum(name, desc, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name,
            final String desc) {
        if (name != null) {
            cp.newUTF8(name);
        }
        cp.newUTF8(desc);
        return new AnnotationConstantsCollector(av.visitAnnotation(name, desc),
                cp);
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        if (name != null) {
            cp.newUTF8(name);
        }
        return new AnnotationConstantsCollector(av.visitArray(name), cp);
    }

    @Override
    public void visitEnd() {
        av.visitEnd();
    }
}