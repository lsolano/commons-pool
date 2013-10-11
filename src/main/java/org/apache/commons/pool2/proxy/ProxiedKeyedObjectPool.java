/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.pool2.proxy;

import java.util.NoSuchElementException;

import org.apache.commons.pool2.KeyedObjectPool;

public class ProxiedKeyedObjectPool<K,V> implements KeyedObjectPool<K,V> {

    private final KeyedObjectPool<K,V> pool;
    private final ProxySource<V> proxySource;


    public ProxiedKeyedObjectPool(KeyedObjectPool<K,V> pool,
            ProxySource<V> proxySource) {
        this.pool = pool;
        this.proxySource = proxySource;
    }


    @Override
    public V borrowObject(K key) throws Exception, NoSuchElementException,
            IllegalStateException {
        V pooledObject = pool.borrowObject(key);
        V proxy = proxySource.createProxy(pooledObject);
        return proxy;
    }

    @Override
    public void returnObject(K key, V proxy) throws Exception {
        V pooledObject = proxySource.resolveProxy(proxy);
        pool.returnObject(key, pooledObject);
    }

    @Override
    public void invalidateObject(K key, V proxy) throws Exception {
        V pooledObject = proxySource.resolveProxy(proxy);
        pool.invalidateObject(key, pooledObject);
    }

    @Override
    public void addObject(K key) throws Exception, IllegalStateException,
            UnsupportedOperationException {
        pool.addObject(key);
    }

    @Override
    public int getNumIdle(K key) {
        return pool.getNumIdle(key);
    }

    @Override
    public int getNumActive(K key) {
        return pool.getNumActive(key);
    }

    @Override
    public int getNumIdle() {
        return pool.getNumIdle();
    }

    @Override
    public int getNumActive() {
        return pool.getNumActive();
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {
        pool.clear();
    }

    @Override
    public void clear(K key) throws Exception, UnsupportedOperationException {
        pool.clear(key);
    }

    @Override
    public void close() {
        pool.close();
    }
}