package hse.kpo.hw.kpohw3shop.controller;

import hse.kpo.hw.kpohw3shop.dto.request.OrderRequest;
import hse.kpo.hw.kpohw3shop.service.AuthService;
import hse.kpo.hw.kpohw3shop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class ShopController {
    private final OrderService orderService;
    private final AuthService authService;

    public ShopController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @PostMapping("/make-order")
    public ResponseEntity<String> makeOrder(@RequestBody OrderRequest request) {
        try {
            Optional<Integer> userId = authService.checkAuthorized(request.getToken());
            if (userId.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
            }

            orderService.makeOrder(request, userId.get());
        }
        catch (ResponseStatusException ex) {
            return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
        }

        return ResponseEntity.ok("The order has been successfully made.");
    }
}
