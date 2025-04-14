package com.kobo.coworker.question.fixture;

import com.kobo.coworker.question.domain.Question;
import static com.kobo.coworker.document.fixture.TestFixture.createSampleDocument;
import static com.kobo.coworker.user.fixture.TestFixture.createSampleUser;

public class TestFixture {

    public Question createSampleQuestion() {
        return Question.builder()
                .document(createSampleDocument())
                .user(createSampleUser())
                .content("오늘 IT 업계 주식 현황 알려줘")
                .build();
    }
}
