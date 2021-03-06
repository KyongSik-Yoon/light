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
package com.geeksaga.light.profiler.util;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;

import java.util.Arrays;

/**
 * @author geeksaga
 * @see org.objectweb.asm.optimizer.Constant;
 */
public class Constant
{
    public char type;

    public int intVal;

    public long longVal;

    public float floatVal;

    public double doubleVal;

    public String strVal1;

    public String strVal2;

    public Object objVal3;

    public Object[] objVals;

    public int hashCode;

    public Constant() {}

    public Constant(final Constant i)
    {
        type = i.type;
        intVal = i.intVal;
        longVal = i.longVal;
        floatVal = i.floatVal;
        doubleVal = i.doubleVal;
        strVal1 = i.strVal1;
        strVal2 = i.strVal2;
        objVal3 = i.objVal3;
        objVals = i.objVals;
        hashCode = i.hashCode;
    }

    /**
     * Sets this item to an integer item.
     *
     * @param intVal the value of this item.
     */
    void set(final int intVal)
    {
        this.type = 'I';
        this.intVal = intVal;
        this.hashCode = 0x7FFFFFFF & (type + intVal);
    }

    /**
     * Sets this item to a long item.
     *
     * @param longVal the value of this item.
     */
    void set(final long longVal)
    {
        this.type = 'J';
        this.longVal = longVal;
        this.hashCode = 0x7FFFFFFF & (type + (int) longVal);
    }

    /**
     * Sets this item to a float item.
     *
     * @param floatVal the value of this item.
     */
    void set(final float floatVal)
    {
        this.type = 'F';
        this.floatVal = floatVal;
        this.hashCode = 0x7FFFFFFF & (type + (int) floatVal);
    }

    /**
     * Sets this item to a double item.
     *
     * @param doubleVal the value of this item.
     */
    void set(final double doubleVal)
    {
        this.type = 'D';
        this.doubleVal = doubleVal;
        this.hashCode = 0x7FFFFFFF & (type + (int) doubleVal);
    }

    /**
     * Sets this item to an item that do not hold a primitive value.
     *
     * @param type    the type of this item.
     * @param strVal1 first part of the value of this item.
     * @param strVal2 second part of the value of this item.
     * @param strVal3 third part of the value of this item.
     */
    void set(final char type, final String strVal1, final String strVal2, final String strVal3)
    {
        this.type = type;
        this.strVal1 = strVal1;
        this.strVal2 = strVal2;
        this.objVal3 = strVal3;
        switch (type)
        {
            case 's':
            case 'S':
            case 'C':
            case 't':
                hashCode = 0x7FFFFFFF & (type + strVal1.hashCode());
                return;
            case 'T':
                hashCode = 0x7FFFFFFF & (type + strVal1.hashCode() * strVal2.hashCode());
                return;
            // case 'G':
            // case 'M':
            // case 'N':
            // case 'h' ... 'p':
            default:
                hashCode = 0x7FFFFFFF & (type + strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode());
        }
    }

    /**
     * Set this item to an InvokeDynamic item.
     *
     * @param name    invokedynamic's name.
     * @param desc    invokedynamic's descriptor.
     * @param bsm     bootstrap method.
     * @param bsmArgs bootstrap method constant arguments.
     */
    void set(final String name, final String desc, final Handle bsm, final Object[] bsmArgs)
    {
        this.type = 'y';
        this.strVal1 = name;
        this.strVal2 = desc;
        this.objVal3 = bsm;
        this.objVals = bsmArgs;

        int hashCode = 'y' + name.hashCode() * desc.hashCode() * bsm.hashCode();
        for (int i = 0; i < bsmArgs.length; i++)
        {
            hashCode *= bsmArgs[i].hashCode();
        }
        this.hashCode = 0x7FFFFFFF & hashCode;
    }

    void write(final ClassWriter cw)
    {
        switch (type)
        {
            case 'I':
                cw.newConst(intVal);
                break;
            case 'J':
                cw.newConst(longVal);
                break;
            case 'F':
                cw.newConst(floatVal);
                break;
            case 'D':
                cw.newConst(doubleVal);
                break;
            case 'S':
                cw.newConst(strVal1);
                break;
            case 's':
                cw.newUTF8(strVal1);
                break;
            case 'C':
                cw.newClass(strVal1);
                break;
            case 'T':
                cw.newNameType(strVal1, strVal2);
                break;
            case 'G':
                cw.newField(strVal1, strVal2, (String) objVal3);
                break;
            case 'M':
                cw.newMethod(strVal1, strVal2, (String) objVal3, false);
                break;
            case 'N':
                cw.newMethod(strVal1, strVal2, (String) objVal3, true);
                break;
            case 'y':
                cw.newInvokeDynamic(strVal1, strVal2, (Handle) objVal3, objVals);
                break;
            case 't':
                cw.newMethodType(strVal1);
                break;
            default: // 'h' ... 'p': handle
                cw.newHandle(type - 'h' + 1, strVal1, strVal2, (String) objVal3);
        }
    }

    @Override
    public boolean equals(final Object o)
    {
        if (!(o instanceof Constant))
        {
            return false;
        }
        Constant c = (Constant) o;
        if (c.type == type)
        {
            switch (type)
            {
                case 'I':
                    return c.intVal == intVal;
                case 'J':
                    return c.longVal == longVal;
                case 'F':
                    return Float.compare(c.floatVal, floatVal) == 0;
                case 'D':
                    return Double.compare(c.doubleVal, doubleVal) == 0;
                case 's':
                case 'S':
                case 'C':
                case 't':
                    return c.strVal1.equals(strVal1);
                case 'T':
                    return c.strVal1.equals(strVal1) && c.strVal2.equals(strVal2);
                case 'y':
                    return c.strVal1.equals(strVal1) && c.strVal2.equals(strVal2) && c.objVal3.equals(objVal3) && Arrays.equals(c.objVals, objVals);
                // case 'G':
                // case 'M':
                // case 'N':
                // case 'h' ... 'p':
                default:
                    return c.strVal1.equals(strVal1) && c.strVal2.equals(strVal2) && c.objVal3.equals(objVal3);
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }
}