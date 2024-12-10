package org.backend.server.controller;

import org.backend.dto.TicketDto;
import org.backend.dto.VendorDto;
import org.backend.enums.FilePaths;
import org.backend.io.file.JsonReader;
import org.backend.model.Ticket;
import org.backend.model.Vendor;
import org.backend.pools.ThreadPool;
import org.backend.pools.TicketPool;
import org.backend.services.VendorSimulation;
import org.backend.thread.ThreadExecutableAbstract;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {
    private final ThreadPool threadPool;
    private final TicketPool ticketPool;

    public VendorController(ThreadPool threadPool, TicketPool ticketPool) {
        this.threadPool = threadPool;
        this.ticketPool = ticketPool;
    }

    @GetMapping("/all-vendor")
    public List<VendorDto> allVendor() {
        return threadPool.findTargetClassThread(VendorSimulation.class).stream().map(VendorSimulation::getVendor).map(Vendor::toDto).toList();
    }

    @GetMapping("/active-vendor")
    public List<VendorDto> activeVendor() {
        return threadPool.findTargetClassThread(VendorSimulation.class).stream().filter(ThreadExecutableAbstract::isRunning).map(VendorSimulation::getVendor).map(Vendor::toDto).toList();
    }

    @GetMapping("/ticket-active/{:id}")
    public List<TicketDto> ticketActive(@PathVariable String id) {
        return ticketPool.findTicketsForVendor(id).stream().map(Ticket::toDto).toList();
    }

    @GetMapping("/ticket-removed/{:id}")
    public List<TicketDto> ticketRemoved(@PathVariable String id) {
        try {
            return JsonReader.readChunkedFiles(FilePaths.CUSTOMER.toString(), "customer", id, TicketDto.class);
        } catch (IOException e) {
            return null;
        }
    }
}
