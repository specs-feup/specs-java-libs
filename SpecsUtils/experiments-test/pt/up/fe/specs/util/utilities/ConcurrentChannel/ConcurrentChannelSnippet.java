/**
 * Copyright 2012 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.utilities.ConcurrentChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

public class ConcurrentChannelSnippet {

    @Test
    public void test() {

	ConcurrentChannel<Integer> integerChannel = new ConcurrentChannel<>(3);

	ChannelProducer<Integer> producerChannel = integerChannel.createProducer();
	ChannelConsumer<Integer> consumerChannel = integerChannel.createConsumer();

	Runnable producer = new Producer(producerChannel);
	Runnable consumer = new Consumer(consumerChannel);

	ExecutorService exService = Executors.newFixedThreadPool(2);
	exService.submit(consumer);
	exService.submit(producer);
	exService.shutdown();
	try {
	    exService.awaitTermination(1, TimeUnit.DAYS);
	} catch (InterruptedException e) {
	    Thread.currentThread().interrupt();
	}

	System.out.println("Hello");

    }

    class Producer implements Runnable {

	ChannelProducer<Integer> producerChannel;

	private Producer(ChannelProducer<Integer> producerChannel) {
	    super();
	    this.producerChannel = producerChannel;
	}

	@Override
	public void run() {

	    for (int i = 0; i < 10; i++) {
		System.out.println("Produced " + i);
		this.producerChannel.offer(i);
	    }
	}

    }

    class Consumer implements Runnable {

	ChannelConsumer<Integer> consumerChannel;

	private Consumer(ChannelConsumer<Integer> consumerChannel) {
	    super();
	    this.consumerChannel = consumerChannel;
	}

	@Override
	public void run() {

	    Integer element = -1;
	    while (!element.equals(9)) {
		try {
		    element = this.consumerChannel.take();
		    System.out.println("Consumed " + element);
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		}

	    }

	}

    }
}
