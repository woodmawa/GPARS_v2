// GPars - Groovy Parallel Systems
//
// Copyright © 2008-2012  The original author or authors
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

package groovyx.gpars.dataflow.operator;

import groovyx.gpars.dataflow.DataflowReadChannel;
import groovyx.gpars.dataflow.DataflowWriteChannel;

import java.util.List;

/**
 * Enables external entities to observe the state of dataflow selectors and operators
 *
 * @author Vaclav Pech
 */
public interface DataflowEventListener {

    /**
     * Invoked immediately after the listener has been attached to a dataflow processor.
     *
     * @param processor The reporting dataflow operator/selector
     */
    void registered(DataflowProcessor processor);

    /**
     * Invoked immediately after the operator starts by a pooled thread before the first message is obtained
     *
     * @param processor The reporting dataflow operator/selector
     */
    void afterStart(DataflowProcessor processor);

    /**
     * Invoked immediately after the operator terminates
     *
     * @param processor The reporting dataflow operator/selector
     */
    void afterStop(DataflowProcessor processor);

    /**
     * Invoked if an exception occurs.
     * If any of the listeners returns true, the operator will terminate.
     * Exceptions outside of the operator's body or listeners' messageSentOut() handlers will terminate the operator irrespective of the listeners' votes.
     * When using maxForks, the method may be invoked from threads running the forks.
     *
     * @param processor The reporting dataflow operator/selector
     * @param e         The thrown exception
     * @return True, if the operator should terminate in response to the exception, false otherwise.
     */
    boolean onException(DataflowProcessor processor, Throwable e);

    /**
     * Invoked when a message becomes available in an input channel.
     *
     * @param processor The reporting dataflow operator/selector
     * @param channel   The input channel holding the message
     * @param index     The index of the input channel within the operator
     * @param message   The incoming message
     * @return The original message or a message that should be used instead
     */
    Object messageArrived(DataflowProcessor processor, DataflowReadChannel<Object> channel, int index, Object message);

    /**
     * Invoked when a control message (instances of ControlMessage) becomes available in an input channel.
     *
     * @param processor The reporting dataflow operator/selector
     * @param channel   The input channel holding the message
     * @param index     The index of the input channel within the operator
     * @param message   The incoming message
     * @return The original message or a message that should be used instead
     */
    Object controlMessageArrived(DataflowProcessor processor, DataflowReadChannel<Object> channel, int index, Object message);

    /**
     * Invoked when a message is being bound to an output channel.
     * When using maxForks, the method may be invoked from threads running the forks.
     *
     * @param processor The reporting dataflow operator/selector
     * @param channel   The output channel to send the message to
     * @param index     The index of the output channel within the operator
     * @param message   The message to send
     * @return The original message or a message that should be used instead
     */
    Object messageSentOut(DataflowProcessor processor, DataflowWriteChannel<Object> channel, int index, Object message);

    /**
     * Invoked when all messages required to trigger the operator become available in the input channels.
     *
     * @param processor The reporting dataflow operator/selector
     * @param messages  The incoming messages
     * @return The original list of messages or a modified/new list of messages that should be used instead
     */
    List<Object> beforeRun(DataflowProcessor processor, List<Object> messages);

    /**
     * Invoked when the operator completes a single run
     * When using maxForks, the method may be invoked from threads running the forks.
     *
     * @param processor The reporting dataflow operator/selector
     * @param messages  The incoming messages that have been processed
     */
    void afterRun(DataflowProcessor processor, List<Object> messages);

    /**
     * Invoked when the fireCustomEvent() method is triggered manually on a dataflow operator/selector
     * When using maxForks, the method may be invoked from threads running the forks.
     *
     * @param processor The reporting dataflow operator/selector
     * @param data      The custom piece of data provided as part of the event
     * @return A value to return from the fireCustomEvent() method to the caller (event initiator)
     */
    Object customEvent(DataflowProcessor processor, Object data);
}
