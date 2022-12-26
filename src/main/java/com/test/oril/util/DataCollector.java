package com.test.oril.util;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

@Configuration
@AllArgsConstructor
public class DataCollector {

	private final Timer timer = new Timer();
	@Autowired
	private TimerTask task;

	@PostConstruct
	public void run() {
		timer.schedule(task, 0, 1000);
	}
}
