package com.kobo.coworker.document.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum FileType {
    PDF(Set.of("pdf")),
    PPT(Set.of("ppt", "pptx")),
    EXCEL(Set.of("xls", "xlsx")),
    WORD(Set.of("doc", "docx"));

    private final Set<String> extensions;
    private static final Map<String, FileType> extensionToFileTypeMap = new HashMap<>();

    static {
        for (FileType fileType : values()) {
            for (String ext : fileType.extensions) {
                extensionToFileTypeMap.put(ext.toLowerCase(), fileType);
            }
        }
    }

    FileType(Set<String> extensions) {
        this.extensions = extensions;
    }

    public static boolean isValidExtension(String extension) {
        return extensionToFileTypeMap.containsKey(extension.toLowerCase());
    }

    public static FileType fromExtension(String extension) {
        FileType fileType = extensionToFileTypeMap.get(extension.toLowerCase());
        if (fileType == null) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + extension);
        }
        return fileType;
    }
}
