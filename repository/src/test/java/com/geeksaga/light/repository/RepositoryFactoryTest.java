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
package com.geeksaga.light.repository;

import com.geeksaga.light.repository.entity.Transaction;
import com.geeksaga.light.repository.factory.RepositoryFactory;
import com.geeksaga.light.util.SystemProperty;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author geeksaga
 */
public class RepositoryFactoryTest
{
    private static RepositoryFactory factory;
    private static final String DEFAULT_PATH = "/../databases/";

    @BeforeClass
    public static void init()
    {
        System.setProperty("light.db.url", String.format("memory:/%s/", Product.NAME.toLowerCase()));
//        System.setProperty("light.db.url", String.format("plocal:.%s", DEFAULT_PATH));

        factory = RepositoryFactory.getInstance(Product.NAME.toLowerCase());
    }

    private String replaceWindowsSeparator(String path)
    {
        if (SystemProperty.WINDOWS_OS && path != null)
        {
            return path.replace("/", File.separator);
        }

        return path;
    }

    @Test
    public void testOSSeparator()
    {
        if(SystemProperty.WINDOWS_OS)
        {
            assertThat(DEFAULT_PATH.replace("/", "\\"), is(replaceWindowsSeparator(DEFAULT_PATH)));
        }
        else
        {
            assertThat(DEFAULT_PATH, is(replaceWindowsSeparator(DEFAULT_PATH)));
        }
    }

    @Test
    public void testGetSameInstance()
    {
        assertThat(factory, is(RepositoryFactory.getInstance(Product.NAME)));
        assertThat(factory.getObjectDatabaseTx(), is(RepositoryFactory.getInstance(Product.NAME).getObjectDatabaseTx()));
    }

    @Test
    public void testFindClass()
    {
        OClass transactionClass = factory.findClass(Transaction.class.getSimpleName());

        assertThat(transactionClass, notNullValue());
        assertThat(transactionClass.existsProperty("tid"), is(true));
    }
}