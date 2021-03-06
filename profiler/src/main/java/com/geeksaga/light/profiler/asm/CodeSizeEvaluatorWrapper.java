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
package com.geeksaga.light.profiler.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.CodeSizeEvaluator;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author geeksaga
 */
public class CodeSizeEvaluatorWrapper extends CodeSizeEvaluator
{
    public MethodVisitor mvWrapper = null;
    public MethodNode method = null;

    public CodeSizeEvaluatorWrapper(final MethodVisitor mv)
    {
        this(Opcodes.ASM5, mv);
    }

    protected CodeSizeEvaluatorWrapper(int api, MethodVisitor mv)
    {
        super(api, mv);

        this.mvWrapper = mv;
        this.method = (MethodNode) mv;
    }
}