package com.metamong.mt.global.jackson.fileextension;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.metamong.mt.global.file.FileType;

public class FileTypeDeserializer extends JsonDeserializer<FileType> {

    @Override
    public FileType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String valueAsString = p.getValueAsString();
        return FileType.valueOf(valueAsString.toUpperCase());
    }
}
