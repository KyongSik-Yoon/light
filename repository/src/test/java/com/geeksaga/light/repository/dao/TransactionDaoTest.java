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
package com.geeksaga.light.repository.dao;

import com.geeksaga.light.repository.connect.RepositorySource;
import com.geeksaga.light.repository.dao.orientdb.TransactionDaoImpl;
import com.geeksaga.light.repository.entity.Transaction;
import com.geeksaga.light.repository.util.IdentifierUtils;
import com.geeksaga.light.test.TestConfigure;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.junit.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author geeksaga
 */
public class TransactionDaoTest
{
    private static RepositorySource repositorySource;
    private TransactionDao transactionDao;

    @BeforeClass
    public static void init()
    {
        TestConfigure.load();

        repositorySource = TestConfigure.getRepositorySource();
    }

    @AfterClass
    public static void destroy()
    {
        OPartitionedDatabasePool partitionedDatabasePool = repositorySource.getPartitionedDatabasePool();

        assertThat(partitionedDatabasePool.getAvailableConnections(), is(partitionedDatabasePool.getCreatedInstances()));
    }

    @Before
    public void setup()
    {
        transactionDao = new TransactionDaoImpl(repositorySource);
    }

    @After
    public void teardown()
    {
        OPartitionedDatabasePool partitionedDatabasePool = repositorySource.getPartitionedDatabasePool();

        System.out.println(partitionedDatabasePool.getAvailableConnections() + " = " + partitionedDatabasePool.getCreatedInstances());
    }

    @Test
    public void testSave()
    {
        OObjectDatabaseTx objectDatabaseTx = repositorySource.getObjectDatabaseTx();

        Transaction transaction = objectDatabaseTx.newInstance(Transaction.class, IdentifierUtils.nextLong());

        transactionDao.save(transaction);

        objectDatabaseTx.close();
    }

    @Test(expected = com.orientechnologies.orient.core.storage.ORecordDuplicatedException.class)
    public void testUniqueIndex()
    {
        OObjectDatabaseTx objectDatabaseTx = repositorySource.getObjectDatabaseTx();

        Transaction transaction = objectDatabaseTx.newInstance(Transaction.class, 1L);

        transactionDao.save(transaction);

        objectDatabaseTx.close();

        objectDatabaseTx = repositorySource.getObjectDatabaseTx();

        try
        {
            transaction = objectDatabaseTx.newInstance(Transaction.class, 1L);

            transactionDao.save(transaction);
        }
        finally
        {
            objectDatabaseTx.close();
        }
    }

    @Test
    public void testFind()
    {
        OObjectDatabaseTx objectDatabaseTx = repositorySource.getObjectDatabaseTx();

        Transaction transaction = objectDatabaseTx.newInstance(Transaction.class, IdentifierUtils.nextLong());

        transactionDao.save(transaction);

        assertThat(transactionDao.find(transaction).getTid(), is(transaction.getTid()));

        objectDatabaseTx.close();
    }

    @Test
    public void testFindList()
    {
        OObjectDatabaseTx objectDatabaseTx = repositorySource.getObjectDatabaseTx();

        Transaction transaction = objectDatabaseTx.newInstance(Transaction.class, IdentifierUtils.nextLong());

        transactionDao.save(transaction);

        assertThat(transactionDao.findList(), notNullValue());
        assertThat(transactionDao.findList().size(), greaterThanOrEqualTo(1));

        objectDatabaseTx.close();
    }
}