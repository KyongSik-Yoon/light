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
package com.geeksaga.light.repository.store;

import com.geeksaga.light.agent.config.ConfigValueDef;
import com.geeksaga.light.repository.Product;
import com.geeksaga.light.repository.entity.Transaction;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

/**
 * @author geeksaga
 */
public class StoreFactory
{
    private static StoreFactory instance = null;
    private static OObjectDatabaseTx objectDatabaseTx = null;

    private static String dbUrl = System.getProperty("light.db.url", ConfigValueDef.db_url);

    private StoreFactory(String database)
    {
        //        initDatabaseDocumentTx(database);
        initObjectDatabaseTx(database);

        initTableClass();
    }

    public static StoreFactory getInstance()
    {
        return getInstance(Product.NAME.toLowerCase());
    }

    public static StoreFactory getInstance(String database)
    {
        if (instance == null)
        {
            instance = new StoreFactory(database);
        }

        return instance;
    }

    public OObjectDatabaseTx getObjectDatabaseTx()
    {
        objectDatabaseTx.activateOnCurrentThread();

        return objectDatabaseTx;
    }

    private void initDatabaseDocumentTx(String database)
    {
        ODatabaseDocumentTx documentTx = new ODatabaseDocumentTx(dbUrl + database);
        if (!documentTx.exists())
        {
            documentTx.create();
        }
    }

    private void initObjectDatabaseTx(String database)
    {
        objectDatabaseTx = new OObjectDatabaseTx(dbUrl + database);
        //        OObjectDatabaseTx db = OObjectDatabasePool.global().acquire(path + database, "admin", "admin");

        if (objectDatabaseTx.exists())
        {
            objectDatabaseTx = new OObjectDatabaseTx(dbUrl + database).open("admin", "admin");
        }
        else
        {
            objectDatabaseTx.create();
        }

        objectDatabaseTx.setAutomaticSchemaGeneration(true);

        objectDatabaseTx.getEntityManager().registerEntityClasses(Transaction.class.getPackage().getName(), StoreFactory.class.getClassLoader());

        objectDatabaseTx.getMetadata().getSchema().synchronizeSchema();
    }

    private void initTableClass()
    {
        // OPartitionedDatabasePool                           instance thread-safe
        //        ODatabaseDocumentTx documentTx = objectDatabaseTx.getDatabase(); // non thread-safe
        OObjectDatabaseTx objectDatabaseTx = StoreFactory.objectDatabaseTx;
        OClass oClass = objectDatabaseTx.getMetadata().getSchema().getClass(Transaction.class);

        if (oClass != null && !oClass.areIndexed("tid"))
        {
            oClass.createIndex("TransactionIdUnique", OClass.INDEX_TYPE.UNIQUE, "tid");
        }
    }

    public OClass findClass(String className)
    {
        //        ODatabaseDocumentTx documentTx = objectDatabaseTx.getDatabase();
        OObjectDatabaseTx documentTx = objectDatabaseTx;
        OSchema schema = documentTx.getMetadata().getSchema();

        return schema.getClass(className);
    }
}
