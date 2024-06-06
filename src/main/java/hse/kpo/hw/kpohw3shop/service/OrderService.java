package hse.kpo.hw.kpohw3shop.service;

import hse.kpo.hw.kpohw3shop.dto.request.OrderRequest;
import hse.kpo.hw.kpohw3shop.entity.Order;
import hse.kpo.hw.kpohw3shop.entity.Station;
import hse.kpo.hw.kpohw3shop.repository.OrderRepository;
import hse.kpo.hw.kpohw3shop.repository.StationRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class OrderService {
    private final OrderRepository orderRepo;
    private final StationRepository stationRepo;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepo, StationRepository stationRepo, RestTemplate restTemplate) {
        this.orderRepo = orderRepo;
        this.stationRepo = stationRepo;
        this.restTemplate = restTemplate;
    }

    public void makeOrder(OrderRequest request) {
        Optional<Station> toStation = stationRepo.findById(request.getToStationId());
        if (toStation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such to station");
        }

        Optional<Station> fromStation = stationRepo.findById(request.getFromStationId());
        if (fromStation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such from station");
        }

        Order order = Order.builder()
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .fromStation(toStation.get())
                .toStation(toStation.get())
                .status(Order.Status.CHECK)
                .build();

        orderRepo.save(order);
    }
}
