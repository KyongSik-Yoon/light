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
package com.geeksaga.light.repository.connect;

import com.geeksaga.light.config.Config;
import com.geeksaga.light.repository.entity.Transaction;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

import static com.geeksaga.light.agent.config.ConfigDefaultValueDef.default_db_url;

/**
 * @author geeksaga
 */
public class RepositoryConnection
{
    private OObjectDatabaseTx objectDatabaseTx = null;
    private Config config;

    private static String dbUrl = System.getProperty("light.db.url", default_db_url);

    public RepositoryConnection(String database)
    {
        this(dbUrl, database);
    }

    public RepositoryConnection(String url, String database)
    {
        dbUrl = url;

        //        initObjectDatabaseTx(database);

        initTableClass();
    }

    public RepositoryConnection(Config config, String database)
    {
        this.config = config;

        //        initObjectDatabaseTx(database);

//        initTableClass();
    }

    public OObjectDatabaseTx getObjectDatabaseTx()
    {
        OObjectDatabaseTx objectDatabaseTx = getDatabase(dbUrl + "7908", "admin", "admin");

        objectDatabaseTx.setAutomaticSchemaGeneration(true);

        objectDatabaseTx.getEntityManager().registerEntityClasses(Transaction.class.getPackage().getName(), RepositoryConnection.class.getClassLoader());

        objectDatabaseTx.getMetadata().getSchema().synchronizeSchema();

        if (!objectDatabaseTx.isActiveOnCurrentThread())
        {
            objectDatabaseTx.activateOnCurrentThread();
        }

        return objectDatabaseTx;
    }


    private OObjectDatabaseTx getDatabase(String database, String user, String password)
    {
        OObjectDatabaseTx objectDatabaseTx = new OObjectDatabaseTx(database);
        if (!objectDatabaseTx.exists())
        {
            objectDatabaseTx.create();

            return objectDatabaseTx;
        }

        return objectDatabaseTx.open(user, password);
    }

    private void initObjectDatabaseTx(String database)
    {
        //        OServerAdmin serverAdmin = new OServerAdmin(dbUrl).connect("root", "root");
        //        serverAdmin.createDatabase(database, "object", "plocal");

        //        ODatabaseDocument database2 = (ODatabaseDocument) ODatabaseRecordThreadLocal.INSTANCE.get();
        getDatabase(dbUrl + database, "admin", "admin");

        objectDatabaseTx = new OObjectDatabaseTx(dbUrl + database);
        objectDatabaseTx.setProperty("minPool", 2);
        objectDatabaseTx.setProperty("maxPool", 5);

        if (objectDatabaseTx.exists())
        {
            System.out.println("open");

            objectDatabaseTx.open("admin", "admin");
        }
        else
        {
            if (objectDatabaseTx.isClosed())
            {
                System.out.println("create");
                objectDatabaseTx.create();
            }
        }

        objectDatabaseTx.setAutomaticSchemaGeneration(true);

        objectDatabaseTx.getEntityManager().registerEntityClasses(Transaction.class.getPackage().getName(), RepositoryConnection.class.getClassLoader());

        objectDatabaseTx.getMetadata().getSchema().synchronizeSchema();
    }

    private void initTableClass()
    {
        // OPartitionedDatabasePool                           instance thread-safe
        //        ODatabaseDocumentTx documentTx = objectDatabaseTx.getDatabase(); // non thread-safe
        OClass oClass = getObjectDatabaseTx().getMetadata().getSchema().getClass(Transaction.class);

        if (oClass != null && !oClass.areIndexed("tid"))
        {
            oClass.createIndex("TransactionIdUnique", OClass.INDEX_TYPE.UNIQUE, "tid");
        }
    }

    public void close()
    {
        if (objectDatabaseTx != null && !objectDatabaseTx.isClosed())
        {
            objectDatabaseTx.close();
        }
    }

    public OClass findClass(String className)
    {
        //        ODatabaseDocumentTx documentTx = objectDatabaseTx.getDatabase();
        System.out.println(getObjectDatabaseTx());
        System.out.println(getObjectDatabaseTx().getMetadata());
        System.out.println(getObjectDatabaseTx().getMetadata().getSchema());

        OSchema schema = getObjectDatabaseTx().getMetadata().getSchema();

        getObjectDatabaseTx().close();

        return schema.getClass(className);
    }
}