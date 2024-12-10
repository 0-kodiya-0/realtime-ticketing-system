package org.backend.server.controller;

import org.backend.pools.ThreadPool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/threads")
public class ThreadController {

    private final ThreadPool threadPool;
    public ThreadController(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @GetMapping("/count/running")
    public int runningThreadCount(){
        return threadPool.getInUseObjects().size();
    }

    @GetMapping("/count/used")
    public int totalUsedThread(){
        return threadPool.findInterruptedThreads().size();
    }

    @GetMapping("/count/interrupted")
    public int totalInterruptedThreads(){
        return threadPool.findRunningThreads().size();
    }
}
