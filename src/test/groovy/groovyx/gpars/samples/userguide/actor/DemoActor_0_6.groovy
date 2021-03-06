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

import groovyx.gpars.actor.Actors
import groovyx.gpars.actor.DefaultActor

/**
 * @author Jan Novotný
 */

DefaultActor me
me = Actors.actor {
    def message = 1

    message.metaClass.onDeliveryError = { ->
        //send message back to the caller
        me << "Could not deliver $delegate"
    }

    def actor = Actors.actor {
        react {
            //wait 2sec in order next call in demo can be emitted
            Thread.sleep(2000)
            //stop actor after first message
            stop()
        }
    }

    actor << message
    actor << message

    react {
        //print whatever comes back
        println it
    }

}

me.join()