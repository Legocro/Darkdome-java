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
package org.apache.mina.common.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;

/**
 * A default implementation of {@link IoFuture}.
 *  
 * @author The Apache MINA Project (dev@mina.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (금, 13  7월 2007) $
 */
public class DefaultIoFuture implements IoFuture {
    private final IoSession session;

    private final Object lock;

    private IoFutureListener firstListener;

    private List<IoFutureListener> otherListeners;

    private Object result;

    private boolean ready;

    /**
     * Creates a new instance.
     * 
     * @param session an {@link IoSession} which is associated with this future
     */
    public DefaultIoFuture(IoSession session) {
        this.session = session;
        this.lock = this;
    }

    public IoSession getSession() {
        return session;
    }

    public void join() {
        synchronized (lock) {
            while (!ready) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public boolean join(long timeoutMillis) {
        long startTime = (timeoutMillis <= 0) ? 0 : System.currentTimeMillis();
        long waitTime = timeoutMillis;

        synchronized (lock) {
            if (ready) {
                return ready;
            } else if (waitTime <= 0) {
                return ready;
            }

            for (;;) {
                try {
                    lock.wait(waitTime);
                } catch (InterruptedException e) {
                }

                if (ready) {
                    return true;
                } else {
                    waitTime = timeoutMillis
                            - (System.currentTimeMillis() - startTime);
                    if (waitTime <= 0) {
                        return ready;
                    }
                }
            }
        }
    }

    public boolean isReady() {
        synchronized (lock) {
            return ready;
        }
    }

    /**
     * Sets the result of the asynchronous operation, and mark it as finished.
     */
    protected void setValue(Object newValue) {
        synchronized (lock) {
            // Allow only once.
            if (ready) {
                return;
            }

            result = newValue;
            ready = true;
            lock.notifyAll();

            notifyListeners();
        }
    }

    /**
     * Returns the result of the asynchronous operation.
     */
    protected Object getValue() {
        synchronized (lock) {
            return result;
        }
    }

    public void addListener(IoFutureListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }

        synchronized (lock) {
            if (firstListener == null) {
                firstListener = listener;
            } else {
                if (otherListeners == null) {
                    otherListeners = new ArrayList<IoFutureListener>(1);
                }
                otherListeners.add(listener);
            }
            if (ready) {
                listener.operationComplete(this);
            }
        }
    }

    public void removeListener(IoFutureListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }

        synchronized (lock) {
            if (listener == firstListener) {
                if (otherListeners != null && !otherListeners.isEmpty()) {
                    firstListener = otherListeners.remove(0);
                } else {
                    firstListener = null;
                }
            } else if (otherListeners != null) {
                otherListeners.remove(listener);
            }
        }
    }

    private void notifyListeners() {
        synchronized (lock) {
            if (firstListener != null) {
                firstListener.operationComplete(this);
                if (otherListeners != null) {
                    for (IoFutureListener l : otherListeners) {
                        l.operationComplete(this);
                    }
                }
            }
        }
    }
}
