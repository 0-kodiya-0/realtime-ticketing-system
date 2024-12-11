package org.backend.server;

import org.backend.dto.MainConfigurationDto;
import org.backend.pools.PurchasePool;
import org.backend.pools.ThreadPool;
import org.backend.pools.TicketPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootServer {

    private ConfigurableApplicationContext run;

    private static TicketPool ticketPool;
    private static PurchasePool vendorPool;
    private static ThreadPool customerPool;
    private static MainConfigurationDto mainConfigurationDto;

    @Bean
    public static TicketPool ticketPool() {
        return ticketPool;
    }

    @Bean
    public static PurchasePool vendorPool() {
        return vendorPool;
    }

    @Bean
    public static ThreadPool customerPool() {
        return customerPool;
    }

    @Bean
    public static MainConfigurationDto mainConfigurationDto() {
        return mainConfigurationDto;
    }

    public void stop() {
        run.stop();
    }

    public void run(TicketPool ticketPool, PurchasePool vendorPool, ThreadPool customerPool, MainConfigurationDto mainConfigurationDto) {
        SpringBootServer.ticketPool = ticketPool;
        SpringBootServer.vendorPool = vendorPool;
        SpringBootServer.customerPool = customerPool;
        SpringBootServer.mainConfigurationDto = mainConfigurationDto;
        run = SpringApplication.run(SpringBootServer.class);
    }
}
