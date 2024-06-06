package hse.kpo.hw.kpohw3shop.dto.request;

import lombok.Data;

@Data
public class OrderRequest {
    // Возможно токен лучше передавать в заголовке, но в условии это не обговорено.
    private String token;
    private int fromStationId;
    private int toStationId;
}
