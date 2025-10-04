package com.rishipadala.journalApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @NotEmpty
    @Schema(description = "User's UserName")
    private String userName;

    @NotEmpty
    @Schema(description = "User's Password")
    private String password;

    @Schema(description = "User's Email")
    private String email;

    @Schema(description = "A boolean flag indicating whether sentiment analysis should be performed on the journal entry's content. Set to true to enable.")
    private boolean sentimentAnalysis;

}
