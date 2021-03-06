// GPars — Groovy Parallel Systems
//
// Copyright © 2008–2010, 2018  The original author or authors
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

package groovyx.gpars.csp.plugAndPlay

import groovyx.gpars.csp.PAR

import org.jcsp.lang.CSProcess
import org.jcsp.lang.ChannelInput
import org.jcsp.lang.ChannelOutput
import org.jcsp.plugNplay.ProcessRead

class GPlus implements CSProcess {

    ChannelOutput outChannel
    ChannelInput inChannel0
    ChannelInput inChannel1

    void run() {

        ProcessRead read0 = new ProcessRead(inChannel0)
        ProcessRead read1 = new ProcessRead(inChannel1)
        def parRead2 = new PAR([read0, read1])

        while (true) {
            parRead2.run()
            outChannel.write(read0.value + read1.value)
        }
    }
}
