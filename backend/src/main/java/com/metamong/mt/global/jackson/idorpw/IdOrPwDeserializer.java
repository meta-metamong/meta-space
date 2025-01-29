package com.metamong.mt.global.jackson.idorpw;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.metamong.mt.domain.member.model.constant.IdOrPw;

public class IdOrPwDeserializer extends JsonDeserializer<IdOrPw> {

    @Override
    public IdOrPw deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String valueAsString = p.getValueAsString();
        return IdOrPw.valueOf(valueAsString.toUpperCase());
    }
}
