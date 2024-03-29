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
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.group.DefaultPGroup

public class NestedClosureTest extends GroovyTestCase {
    public void testNestedClosures() {
        final def result = new DataflowVariable<Integer>()

        final def group = new DefaultPGroup(20)

        final def actor = group.actor {
            final def nestedActor = group.actor {
                react {
                    reply 20
                }
            }
            result << nestedActor.sendAndWait(10)
        }
        assert 20 == result.val
        group.shutdown()
    }
}
