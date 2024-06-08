package hse.kpo.hw.kpohw3shop.service;

import hse.kpo.hw.kpohw3shop.dto.request.OrderRequest;
import hse.kpo.hw.kpohw3shop.dto.response.OrderInfo;
import hse.kpo.hw.kpohw3shop.dto.response.OrderInfoResponse;
import hse.kpo.hw.kpohw3shop.entity.Order;
import hse.kpo.hw.kpohw3shop.entity.Station;
import hse.kpo.hw.kpohw3shop.repository.OrderRepository;
import hse.kpo.hw.kpohw3shop.repository.StationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final StationRepository stationRepo;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepo, StationRepository stationRepo, RestTemplate restTemplate) {
        this.orderRepo = orderRepo;
        this.stationRepo = stationRepo;
        this.restTemplate = restTemplate;
    }

    public int makeOrder(OrderRequest request, int userId) {
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
                .fromStation(fromStation.get())
                .toStation(toStation.get())
                .status(Order.Status.CHECK)
                .userId(userId)
                .build();

        int id = orderRepo.save(order).getId();

        // Ждем 30 секунд и меняем статус.
        new Thread(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ignored) {
            }

            // Мы не удаляем наш заказ из базы, поэтому можем спокойно сразу сделать get().
            Order orderToChange = orderRepo.findById(id).get();
            Random rand = new Random();

            if (rand.nextBoolean()) {
                orderToChange.setStatus(Order.Status.SUCCESS);
            }
            else {
                orderToChange.setStatus(Order.Status.REJECTION);
            }

            orderRepo.save(orderToChange);
        }).start();

        return id;
    }

    public OrderInfoResponse getOrderInfo(int id) {
        Optional<Order> orderOptional = orderRepo.findById(id);

        if (orderOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such order.");
        }

        Order order = orderOptional.get();

        String nickname = getUserName(order.getUserId());

        return new OrderInfoResponse(OrderInfo
                .builder()
                .creationDate(order.getCreated().toString())
                .fromStation(order.getFromStation())
                .toStation(order.getToStation())
                .status(order.getStatus().toString())
                .nickname(nickname)
                .build(),

                "Success."
        );
    }

    private String getUserName(int userId) {
        String url = "http://localhost:8081/nickname?id={userId}";

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.getForEntity(url, String.class, userId);

            if (responseEntity.hasBody() && responseEntity.getBody() != null) {
                return responseEntity.getBody();
            }
        }
        catch (RestClientException ignored) {
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Can't get response from auth service.");
    }
}
