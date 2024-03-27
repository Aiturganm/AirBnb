package project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AcceptOrRejectReq {
    @NotBlank
    private String reason;
}
