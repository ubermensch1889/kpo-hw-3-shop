package hse.kpo.hw.kpohw3shop.dto.response;

import hse.kpo.hw.kpohw3shop.entity.Station;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderInfo {
    private String creationDate;
    private Station fromStation;
    private Station toStation;
    private String status;
    private String nickname;
}
