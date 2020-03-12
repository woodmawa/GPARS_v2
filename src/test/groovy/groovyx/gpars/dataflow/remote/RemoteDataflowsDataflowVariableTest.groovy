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

package groovyx.gpars.dataflow.remote

import groovyx.gpars.dataflow.DataflowVariable
import spock.lang.Specification

class RemoteDataflowsDataflowVariableTest extends Specification {
    RemoteDataflows remoteDataflows

    void setup() {
        remoteDataflows = RemoteDataflows.create()
    }

    def "retrieving not published DataflowVariable returns null"() {
        when:
        def var = remoteDataflows.get DataflowVariable, "test-variable"

        then:
        var == null
    }

    def "can publish DataflowVariable"() {
        setup:
        DataflowVariable<String> var = new DataflowVariable<>()
        def varName = "test-variable"

        when:
        remoteDataflows.publish var, varName
        def publishedVar = remoteDataflows.get DataflowVariable, varName

        then:
        publishedVar == var
    }

    def "retrieving DataflowVariable from remote host returns Promise"() {
        setup:
        def PORT = 9020
        def varName = "test-variable"

        when:
        def varFuture = remoteDataflows.getVariable getHostAddress(), PORT, varName

        then:
        varFuture != null
        varFuture instanceof DataflowVariable
    }

    def "retrieving DataflowVariable from remote host returns the same Promise"() {
        setup:
        def PORT = 9020
        def varName = "test-variable"

        when:
        def varFuture1 = remoteDataflows.getVariable getHostAddress(), PORT, varName
        def varFuture2 = remoteDataflows.getVariable getHostAddress(), PORT, varName

        then:
        varFuture1.is(varFuture2)
    }

    String getHostAddress() {
        InetAddress.getLocalHost().getHostAddress()
    }

}
