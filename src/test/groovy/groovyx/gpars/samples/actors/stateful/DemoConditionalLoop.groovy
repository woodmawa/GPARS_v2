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

package groovyx.gpars.samples.actors.stateful

import groovyx.gpars.actor.Actor
import static groovyx.gpars.actor.Actors.actor

/**
 * Demonstrates the use of conditional actor loops.
 * The following actor will receive messages until a value greater then 30 arrives.
 *
 * @author Vaclav Pech
 * Date: Aug 25th 2010
 */

final Actor actor = actor {
    def candidates = []
    final Closure printResult = { -> println "Reached best offer - ${candidates.max()}" }

    loop({ -> candidates.max() < 30 }, printResult) {
        react {
            candidates << it
        }
    }
}

actor 10
actor 20
actor 25
actor 31
actor 20
actor.join()

