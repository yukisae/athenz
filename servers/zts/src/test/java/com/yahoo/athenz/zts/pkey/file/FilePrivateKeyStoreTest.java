/**
 * Copyright 2016 Yahoo Inc.
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
package com.yahoo.athenz.zts.pkey.file;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import com.yahoo.athenz.zts.ResourceException;
import com.yahoo.athenz.zts.ZTSConsts;
import com.yahoo.athenz.zts.pkey.PrivateKeyStore;

public class FilePrivateKeyStoreTest {

    @Test
    public void testCreateStore() {
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");
        assertNotNull(store);
    }

    @Test
    public void testGetHostPrivateKeyExist() {

        // set default filepath property
        System.setProperty(ZTSConsts.ZTS_PROP_PRIVATE_KEY, "src/test/resources/zts_private.pem");

        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        StringBuilder sbuilder = new StringBuilder();

        assertNotNull(store.getHostPrivateKey(sbuilder));
    }

    @Test
    public void testGetHostPrivateKeyPkeyNotExist() {
        // set default filepath property
        System.setProperty(ZTSConsts.ZTS_PROP_PRIVATE_KEY, "src/test/resources/unknown.pem");

        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        StringBuilder sbuilder = new StringBuilder();

        try {
            store.getHostPrivateKey(sbuilder);
        } catch (RuntimeException ex) {
            assertNotNull(ex.getMessage());
        }

        // fix property
        System.setProperty(ZTSConsts.ZTS_PROP_PRIVATE_KEY, "src/test/resources/zts_private.pem");

    }

    @Test
    public void testGetHostPrivateKeyInvalidFormat() {
        // set default filepath property
        System.setProperty(ZTSConsts.ZTS_PROP_PRIVATE_KEY, "src/test/resources/test_public.v1");

        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        StringBuilder sbuilder = new StringBuilder();

        try {
            store.getHostPrivateKey(sbuilder);
        } catch (ResourceException ex) {
            assertNotNull(ex.getCode());
        }

        // fix property
        System.setProperty(ZTSConsts.ZTS_PROP_PRIVATE_KEY, "src/test/resources/zts_private.pem");

    }

    @Test
    public void testGetPrivateKeyExist() {
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        assertNotNull(store.getPrivateKey("src/test/resources/test_private", 1));

    }

    @Test
    public void testGetPrivateKeyInvalidFormat() {
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        try {
            store.getPrivateKey("src/test/resources/test_public", 1);
            fail();
        } catch (ResourceException ex) {
            assertEquals(ex.getCode(), 500);
        }
    }

    @Test
    public void testGetPublicKeyExist() {
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        assertNotNull(store.getPublicKey("src/test/resources/test_public", 1));
    }

    @Test
    public void testGetPublicKeyInvalidFormat() {
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        // not validate pubkey format
        assertNotNull(store.getPublicKey("src/test/resources/test_private", 1));
    }

    @Test
    public void testGetPublicKeyNotExist() {
        FilePrivateKeyStoreFactory factory = new FilePrivateKeyStoreFactory();
        PrivateKeyStore store = factory.create("localhost");

        try {
            store.getPublicKey("src/test/resources/test_public", 2);
            fail();
        } catch (ResourceException ex) {
            assertEquals(ex.getCode(), 500);
        }
    }

}
