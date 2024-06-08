package hse.kpo.hw.kpohw3shop.dto.response;

import hse.kpo.hw.kpohw3shop.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderInfoResponse {
    private OrderInfo info;
    private String statusMessage;
}
