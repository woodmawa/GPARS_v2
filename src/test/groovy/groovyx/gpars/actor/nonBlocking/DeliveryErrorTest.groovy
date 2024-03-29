// GPars - Groovy Parallel Systems
//
// Copyright © 2008-11  The original author or authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package groovyx.gpars.actor.nonBlocking

import groovy.test.GroovyTestCase
import groovyx.gpars.actor.Actor
import groovyx.gpars.group.PGroupBuilder
import groovyx.gpars.scheduler.DefaultPool
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.atomic.AtomicBoolean

public class DeliveryErrorTest extends GroovyTestCase {

    def group

    protected void setUp() {
        super.setUp();
        group = PGroupBuilder.createFromPool(new DefaultPool(true, 5))
    }

    protected void tearDown() {
        super.tearDown();
        group.shutdown()
    }

    public void testSuccessfulMessages() {
        AtomicBoolean flag = new AtomicBoolean()
        CountDownLatch latch = new CountDownLatch(1)

        final Actor actor = group.actor {
            react {}
        }

        actor.metaClass.afterStop = {
            latch.countDown()
        }
        def message = 1
        message.metaClass.onDeliveryError = { ->
            flag.set(true)
        }
        actor << message

        latch.await()
        assertFalse flag.get()
    }

    public void testFailedMessages() {
        AtomicBoolean flag1 = new AtomicBoolean()
        AtomicBoolean flag2 = new AtomicBoolean()
        CountDownLatch latch = new CountDownLatch(1)
        final CyclicBarrier barrier = new CyclicBarrier(2)

        final Actor actor = group.actor {
            react {
                barrier.await()
            }
        }

        actor.metaClass.afterStop = {
            latch.countDown()
        }

        def message1 = 1
        message1.metaClass.onDeliveryError = { ->
            flag1.set(true)
        }

        def message2 = 2
        message2.metaClass.onDeliveryError = { ->
            flag2.set(true)
        }
        actor << message1
        actor << message2
        barrier.await()

        latch.await()
        assertFalse flag1.get()
        assert flag2.get()
    }

    public void testFailedMessagesOnException() {
        AtomicBoolean flag1 = new AtomicBoolean()
        AtomicBoolean flag2 = new AtomicBoolean()
        CountDownLatch latch = new CountDownLatch(1)
        final CyclicBarrier barrier = new CyclicBarrier(2)

        final Actor actor = group.actor {
            delegate.metaClass.onException = {}
            delegate.metaClass.afterStop = {
                latch.countDown()
            }

            barrier.await()
            react {
                barrier.await()
                throw new RuntimeException('test')
            }
        }

        barrier.await()

        def message1 = 1
        message1.metaClass.onDeliveryError = { ->
            flag1.set(true)
        }

        def message2 = 2
        message2.metaClass.onDeliveryError = { ->
            flag2.set(true)
        }
        actor << message1
        actor << message2
        barrier.await()

        latch.await()
        assertFalse flag1.get()
        assert flag2.get()
    }

    public void testMessagesWithoutAfterStop() {
        AtomicBoolean flag = new AtomicBoolean()
        CountDownLatch latch = new CountDownLatch(1)

        final Actor actor = group.actor {
            latch.await()
        }

        def message = 1
        message.metaClass.onDeliveryError = { ->
            flag.set(true)
        }
        actor << message
        latch.countDown()
        Thread.sleep 1000
        assert flag.get()
    }

    public void testInterruptionFlag() {
        AtomicBoolean flag = new AtomicBoolean()
        CountDownLatch latch = new CountDownLatch(1)

        final Actor actor = group.actor {
            latch.await()
            stop()
        }

        def message = 1
        message.metaClass.onDeliveryError = { ->
            flag.set(Thread.currentThread().isInterrupted())
        }
        actor << message
        latch.countDown()
        Thread.sleep 1000
        assertFalse flag.get()
    }
}
