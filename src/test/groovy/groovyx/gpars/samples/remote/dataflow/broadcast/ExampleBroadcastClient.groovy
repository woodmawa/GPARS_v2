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

package groovyx.gpars.samples.remote.dataflow.broadcast

import groovyx.gpars.dataflow.remote.RemoteDataflows

def HOST = "localhost"
def PORT = 9101

println "Example: DataflowBroadcast"

def remoteDataflows = RemoteDataflows.create()

def streamPromise = remoteDataflows.getReadChannel HOST, PORT, "broadcast"
def stream = streamPromise.get()

println "Value from stream: ${stream.val}"
println "Value from stream: ${stream.val}"
println "Value from stream: ${stream.val}"