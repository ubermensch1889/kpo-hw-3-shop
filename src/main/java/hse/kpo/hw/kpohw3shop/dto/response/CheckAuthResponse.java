package hse.kpo.hw.kpohw3shop.dto.response;

import lombok.Data;

@Data
public class CheckAuthResponse {
    private int userId;
    private Boolean isAuthorized;
}
