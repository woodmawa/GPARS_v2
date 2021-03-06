// GPars - Groovy Parallel Systems
//
// Copyright © 2008-10  The original author or authors
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

package groovyx.gpars.samples.userguide.actor

import groovyx.gpars.actor.DefaultActor

/**
 * @author Vaclav Pech
 */

class MyLoopingActor extends DefaultActor {

    protected void act() {
        loop {
            outerLoop()
        }
    }

    private void outerLoop() {
        react { a ->
            println 'Outer: ' + a
            if (a != 0) innerLoop()
            else println 'Done for now, but will loop again'
        }
    }

    private void innerLoop() {
        react { b ->
            println 'Inner ' + b
            if (b == 0) outerLoop()
            else innerLoop()
        }
    }
}

final def actor = new MyLoopingActor().start()
actor 10
actor 20
actor 0
actor 0
actor 10
actor.stop()
actor.join()
