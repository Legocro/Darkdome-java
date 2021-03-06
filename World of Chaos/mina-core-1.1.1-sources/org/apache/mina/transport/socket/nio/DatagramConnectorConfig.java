/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.transport.socket.nio;

import org.apache.mina.common.ExpiringSessionRecycler;
import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.IoSessionRecycler;
import org.apache.mina.common.RuntimeIOException;
import org.apache.mina.common.support.BaseIoConnectorConfig;
import org.apache.mina.transport.socket.nio.support.DatagramSessionConfigImpl;

/**
 * An {@link IoConnectorConfig} for {@link DatagramConnector}.
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (금, 13  7월 2007) $
 */
public class DatagramConnectorConfig extends BaseIoConnectorConfig implements
        DatagramServiceConfig {
    private static final IoSessionRecycler DEFAULT_RECYCLER = new ExpiringSessionRecycler();

    /**
     * Current session recycler
     */
    private IoSessionRecycler sessionRecycler = DEFAULT_RECYCLER;

    private DatagramSessionConfig sessionConfig = new DatagramSessionConfigImpl();

    /**
     * Creates a new instance.
     *
     * @throws RuntimeIOException if failed to get the default configuration
     */
    public DatagramConnectorConfig() {
        super();
    }

    public DatagramSessionConfig getSessionConfig() {
        return sessionConfig;
    }

    public IoSessionRecycler getSessionRecycler() {
        return sessionRecycler;
    }

    // FIXME There can be a problem if a user changes the recycler after the service is activated.
    public void setSessionRecycler(IoSessionRecycler sessionRecycler) {
        if (sessionRecycler == null) {
            sessionRecycler = DEFAULT_RECYCLER;
        }
        this.sessionRecycler = sessionRecycler;
    }

    public Object clone() {
        DatagramConnectorConfig ret = (DatagramConnectorConfig) super.clone();
        ret.sessionConfig = (DatagramSessionConfig) this.sessionConfig.clone();
        return ret;
    }
}
