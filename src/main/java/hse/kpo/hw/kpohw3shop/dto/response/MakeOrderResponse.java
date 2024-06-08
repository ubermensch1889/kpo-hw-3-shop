package hse.kpo.hw.kpohw3shop.dto.response;

import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MakeOrderResponse {
    private String message;
    private Integer id;
}
