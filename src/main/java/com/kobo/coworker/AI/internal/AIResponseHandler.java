package com.kobo.coworker.AI.internal;

import com.kobo.coworker.common.apiPayload.code.status.ErrorStatus;
import com.kobo.coworker.common.apiPayload.exception.GeneralException;
import java.net.http.HttpResponse;

public class AIResponseHandler {

    public String handle(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new GeneralException(ErrorStatus.AI_SERVER_COMMUNICATION_ERROR);
        }
        return response.body();
    }
}
