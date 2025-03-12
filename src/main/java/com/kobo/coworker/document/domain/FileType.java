package com.kobo.coworker.document.domain;

import java.util.EnumSet;

public enum FileType {
    PDF,
    PPT,
    EXCEL,
    WORD;

    private static final EnumSet<FileType> SUPPORTED_TYPES = EnumSet.allOf(FileType.class);

    public static boolean isValid(FileType fileType) {
        return SUPPORTED_TYPES.contains(fileType);
    }
}
