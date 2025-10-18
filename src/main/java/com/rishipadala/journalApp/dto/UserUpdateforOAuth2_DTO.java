package com.rishipadala.journalApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserUpdateforOAuth2_DTO  {
    @Schema(description = "A boolean flag indicating whether sentiment analysis should be performed on the journal entry's content. Set to true to enable.")
    private Boolean sentimentAnalysis;
}
