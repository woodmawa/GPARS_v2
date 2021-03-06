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

package groovyx.gpars.dataflow

import groovyx.gpars.dataflow.operator.PoisonPill
import static groovyx.gpars.dataflow.Dataflow.operator
import groovy.test.GroovyTestCase


class DataflowChannelFilterTest extends GroovyTestCase {

    public void testCreationFromQueue() {
        final DataflowQueue queue = new DataflowQueue()
        final DataflowReadChannel filteredQueue = new DataflowQueue()
        operator(queue, filteredQueue) {
            if (it < 5) bindOutput it
        }
        queue.bind 10
        queue.bind 20
        queue.bind 1
        queue.bind 2
        queue.bind 5
        queue.bind 3
        assert 1 == filteredQueue.val
        assert 2 == filteredQueue.val
        assert 3 == filteredQueue.val
        assert !filteredQueue.isBound()
        queue.bind(PoisonPill.instance)
    }

    public void testCreationFromBroadCast() {
        final DataflowBroadcast queue = new DataflowBroadcast()
        final DataflowReadChannel filteredQueue = new DataflowQueue()
        operator(queue.createReadChannel(), filteredQueue) {
            if (it < 5) bindOutput it
        }
        queue.bind 10
        queue.bind 20
        queue.bind 1
        queue.bind 2
        queue.bind 5
        queue.bind 3
        assert 1 == filteredQueue.val
        assert 2 == filteredQueue.val
        assert 3 == filteredQueue.val
        assert !filteredQueue.isBound()
        queue.bind(PoisonPill.instance)
    }
}
