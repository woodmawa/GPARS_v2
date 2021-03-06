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
import org.jcsp.lang.Channel
import org.jcsp.lang.ChannelInput
import org.jcsp.lang.ChannelOutput
import org.jcsp.lang.One2OneChannel

class GIntegrate implements CSProcess {

    ChannelOutput outChannel
    ChannelInput inChannel

    void run() {

        One2OneChannel a = Channel.one2one()
        One2OneChannel b = Channel.one2one()
        One2OneChannel c = Channel.one2one()

        def integrateList = [new GPrefix(prefixValue: 0,
                outChannel: c.out(),
                inChannel: b.in()),
                             new GPCopy(inChannel: a.in(),
                                     outChannel0: outChannel,
                                     outChannel1: b.out()),
                             new GPlus(inChannel0: inChannel,
                                     inChannel1: c.in(),
                                     outChannel: a.out())
        ]
        new PAR(integrateList).run()

    }
}
