// GPars - Groovy Parallel Systems
//
// Copyright © 2014  The original author or authors
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

package groovyx.gpars.samples.remote.actor.calculator

import groovyx.gpars.actor.Actors
import groovyx.gpars.actor.remote.RemoteActors

def HOST = "localhost"
def PORT = 9000

def remoteActors = RemoteActors.create()

def queryActor = Actors.actor {
    println "Remote Calculator - Query"

    def remoteCalculator = remoteActors.get HOST, PORT, "remote-calculator" get()

    remoteCalculator << 1
    remoteCalculator << 2

    react { println it }
}

queryActor.join()