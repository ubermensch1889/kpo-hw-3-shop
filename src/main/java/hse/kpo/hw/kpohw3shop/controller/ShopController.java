package hse.kpo.hw.kpohw3shop.controller;

import hse.kpo.hw.kpohw3shop.dto.request.OrderRequest;
import hse.kpo.hw.kpohw3shop.dto.response.MakeOrderResponse;
import hse.kpo.hw.kpohw3shop.dto.response.OrderInfoResponse;
import hse.kpo.hw.kpohw3shop.service.AuthService;
import hse.kpo.hw.kpohw3shop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<MakeOrderResponse> makeOrder(@RequestBody OrderRequest request) {
        try {
            Optional<Integer> userId = authService.checkAuthorized(request.getToken());
            if (userId.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
            }

            int id = orderService.makeOrder(request, userId.get());

            return ResponseEntity.ok(new MakeOrderResponse("The order has been successfully made.", id));
        }
        catch (ResponseStatusException ex) {
            return new ResponseEntity<>(new MakeOrderResponse(ex.getMessage(), null), ex.getStatusCode());
        }
    }

    @GetMapping("/order-info")
    public ResponseEntity<OrderInfoResponse> getOrderInfo(@RequestParam int id) {
        try {
            return ResponseEntity.ok(orderService.getOrderInfo(id));
        }
        catch (ResponseStatusException ex) {
            return new ResponseEntity<>(new OrderInfoResponse(null, ex.getMessage()), ex.getStatusCode());
        }
    }
}
