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

import org.apache.commons.pool2.ObjectPool;

public class ProxiedObjectPool<T> implements ObjectPool<T> {

    private final ObjectPool<T> pool;
    private final ProxySource<T> proxySource;


    public ProxiedObjectPool(ObjectPool<T> pool, ProxySource<T> proxySource) {
        this.pool = pool;
        this.proxySource = proxySource;
    }


    // --------------------------------------------------- ObjectPool<T> methods

    @Override
    public T borrowObject() throws Exception, NoSuchElementException,
            IllegalStateException {
        T pooledObject = pool.borrowObject();
        T proxy = proxySource.createProxy(pooledObject);
        return proxy;
    }


    @Override
    public void returnObject(T proxy) throws Exception {
        T pooledObject = proxySource.resolveProxy(proxy);
        pool.returnObject(pooledObject);
    }


    @Override
    public void invalidateObject(T proxy) throws Exception {
        T pooledObject = proxySource.resolveProxy(proxy);
        pool.invalidateObject(pooledObject);
    }


    @Override
    public void addObject() throws Exception, IllegalStateException,
            UnsupportedOperationException {
        pool.addObject();
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
    public void close() {
        pool.close();
    }
}