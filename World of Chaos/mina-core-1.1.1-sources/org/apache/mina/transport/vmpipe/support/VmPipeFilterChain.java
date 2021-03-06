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
package org.apache.mina.transport.vmpipe.support;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.IoFilter.WriteRequest;
import org.apache.mina.common.support.AbstractIoFilterChain;

/**
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (금, 13  7월 2007) $
 */
public class VmPipeFilterChain extends AbstractIoFilterChain {

    private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<Event>();

    private boolean flushEnabled;

    public VmPipeFilterChain(IoSession session) {
        super(session);
    }

    public void start() {
        flushEnabled = true;
        flushEvents();
    }

    private void pushEvent(Event e) {
        eventQueue.offer(e);
        if (flushEnabled) {
            flushEvents();
        }
    }

    private void flushEvents() {
        Event e;
        while ((e = eventQueue.poll()) != null) {
            fireEvent(e);
        }
    }

    private void fireEvent(Event e) {
        IoSession session = getSession();
        EventType type = e.getType();
        Object data = e.getData();

        if (type == EventType.RECEIVED) {
            VmPipeSessionImpl s = (VmPipeSessionImpl) session;
            synchronized (s.lock) {
                if (!s.getTrafficMask().isReadable()) {
                    s.pendingDataQueue.offer(data);
                } else {
                    int byteCount = 1;
                    if (data instanceof ByteBuffer) {
                        byteCount = ((ByteBuffer) data).remaining();
                    }

                    s.increaseReadBytes(byteCount);

                    super.fireMessageReceived(s, data);
                }
            }
        } else if (type == EventType.WRITE) {
            super.fireFilterWrite(session, (WriteRequest) data);
        } else if (type == EventType.SENT) {
            super.fireMessageSent(session, (WriteRequest) data);
        } else if (type == EventType.EXCEPTION) {
            super.fireExceptionCaught(session, (Throwable) data);
        } else if (type == EventType.IDLE) {
            super.fireSessionIdle(session, (IdleStatus) data);
        } else if (type == EventType.OPENED) {
            super.fireSessionOpened(session);
        } else if (type == EventType.CREATED) {
            super.fireSessionCreated(session);
        } else if (type == EventType.CLOSED) {
            super.fireSessionClosed(session);
        } else if (type == EventType.CLOSE) {
            super.fireFilterClose(session);
        }
    }

    @Override
    public void fireFilterClose(IoSession session) {
        pushEvent(new Event(EventType.CLOSE, null));
    }

    @Override
    public void fireFilterWrite(IoSession session, WriteRequest writeRequest) {
        pushEvent(new Event(EventType.WRITE, writeRequest));
    }

    @Override
    public void fireExceptionCaught(IoSession session, Throwable cause) {
        pushEvent(new Event(EventType.EXCEPTION, cause));
    }

    @Override
    public void fireMessageSent(IoSession session, WriteRequest request) {
        pushEvent(new Event(EventType.SENT, request));
    }

    @Override
    public void fireSessionClosed(IoSession session) {
        pushEvent(new Event(EventType.CLOSED, null));
    }

    @Override
    public void fireSessionCreated(IoSession session) {
        pushEvent(new Event(EventType.CREATED, null));
    }

    @Override
    public void fireSessionIdle(IoSession session, IdleStatus status) {
        pushEvent(new Event(EventType.IDLE, status));
    }

    @Override
    public void fireSessionOpened(IoSession session) {
        pushEvent(new Event(EventType.OPENED, null));
    }

    @Override
    public void fireMessageReceived(IoSession session, Object message) {
        pushEvent(new Event(EventType.RECEIVED, message));
    }

    @Override
    protected void doWrite(IoSession session, WriteRequest writeRequest) {
        VmPipeSessionImpl s = (VmPipeSessionImpl) session;
        synchronized (s.lock) {
            if (s.isConnected()) {

                if (!s.getTrafficMask().isWritable()) {
                    s.pendingDataQueue.offer(writeRequest);
                } else {
                    Object message = writeRequest.getMessage();

                    int byteCount = 1;
                    Object messageCopy = message;
                    if (message instanceof ByteBuffer) {
                        ByteBuffer rb = (ByteBuffer) message;
                        rb.mark();
                        byteCount = rb.remaining();
                        ByteBuffer wb = ByteBuffer.allocate(rb.remaining());
                        wb.put(rb);
                        wb.flip();
                        rb.reset();
                        messageCopy = wb;
                    }

                    s.increaseWrittenBytes(byteCount);
                    s.increaseWrittenMessages();

                    s.getRemoteSession().getFilterChain().fireMessageReceived(
                            s.getRemoteSession(), messageCopy);
                    s.getFilterChain().fireMessageSent(s, writeRequest);
                }
            } else {
                writeRequest.getFuture().setWritten(false);
            }
        }
    }

    @Override
    protected void doClose(IoSession session) {
        VmPipeSessionImpl s = (VmPipeSessionImpl) session;
        synchronized (s.lock) {
            if (!session.getCloseFuture().isClosed()) {
                s.getServiceListeners().fireSessionDestroyed(s);
                s.getRemoteSession().close();
            }
        }
    }

    // FIXME Copied and pasted from {@link ExecutorFilter}.
    private static class EventType {
        public static final EventType CREATED = new EventType("CREATED");

        public static final EventType OPENED = new EventType("OPENED");

        public static final EventType CLOSED = new EventType("CLOSED");

        public static final EventType RECEIVED = new EventType("RECEIVED");

        public static final EventType SENT = new EventType("SENT");

        public static final EventType IDLE = new EventType("IDLE");

        public static final EventType EXCEPTION = new EventType("EXCEPTION");

        public static final EventType WRITE = new EventType("WRITE");

        public static final EventType CLOSE = new EventType("CLOSE");

        private final String value;

        private EventType(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    private static class Event {
        private final EventType type;

        private final Object data;

        public Event(EventType type, Object data) {
            this.type = type;
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public EventType getType() {
            return type;
        }
    }
}
