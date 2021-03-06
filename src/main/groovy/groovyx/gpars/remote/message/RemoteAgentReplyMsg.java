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

package groovyx.gpars.remote.message;

import groovyx.gpars.agent.Agent;
import groovyx.gpars.agent.AgentCore;
import groovyx.gpars.agent.remote.RemoteAgent;
import groovyx.gpars.remote.RemoteConnection;
import groovyx.gpars.serial.SerialMsg;

/**
 * Message that carry Actor.
 *
 * @author Rafal Slawik
 */
@SuppressWarnings({"serial"})
public class RemoteAgentReplyMsg extends SerialMsg {
    private final String name;
    private final AgentCore agent;

    public RemoteAgentReplyMsg(String name, Agent<?> agent) {
        this.name = name;
        this.agent = agent;
    }

    @Override
    public void execute(RemoteConnection conn) {
        updateRemoteHost(conn);
        conn.getLocalHost().registerProxy(RemoteAgent.class, name, ((RemoteAgent) agent));
    }
}
