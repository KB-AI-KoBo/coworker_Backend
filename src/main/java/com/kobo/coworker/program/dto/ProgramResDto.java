package com.kobo.coworker.program.dto;

import com.kobo.coworker.program.domain.SupportProgram;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProgramResDto {
    private List<SupportProgram> data;
}

