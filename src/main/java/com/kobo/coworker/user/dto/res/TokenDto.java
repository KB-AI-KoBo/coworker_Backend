package com.kobo.coworker.user.dto.res;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Builder
@AllArgsConstructor
@Data
public class TokenDto {

    @SerializedName("access_token")
    String access_token;
    @SerializedName("refresh_token")
    private String refresh_token;

}
