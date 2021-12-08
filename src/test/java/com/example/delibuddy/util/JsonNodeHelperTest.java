package com.example.delibuddy.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.example.delibuddy.util.JsonNodeHelper.extractTextOrNull;
import static org.assertj.core.api.Assertions.assertThat;


class JsonNodeHelperTest {

    @Test
    public void 필드가_없을땐_null_return() throws JsonParseException, IOException {

        String jsonString =
        "{\n" +
        "  \"kakao_account\": {\n" +
        "    \"profile\": {\n" +
        "      \"nickname\": \"정승원\",\n" +
        "      \"thumbnail_image_url\": \"http://k.kakaocdn.net/dn/kHjrj/btrb9Q6qEfo/c7XSqWKFj8leYyYygH3k61/img_110x110.jpg\",\n" +
        "      \"profile_image_url\": \"http://k.kakaocdn.net/dn/kHjrj/btrb9Q6qEfo/c7XSqWKFj8leYyYygH3k61/img_640x640.jpg\",\n" +
        "      \"is_default_image\": false\n" +
        "    }\n" +
        "  }\n" +
        "}";

        JsonNode node = new ObjectMapper().readTree(jsonString);
        assertThat(extractTextOrNull(node, Arrays.asList("kakao_account", "email"))).isEqualTo(null);
    }

    @Test
    public void 필드가_있을떈_값을_return() throws JsonParseException, IOException {

        String jsonString =
        "{\n" +
        "  \"kakao_account\": {\n" +
        "    \"email\": \"aliwo@naver.com\"\n" +
        "  }\n" +
        "}";

        JsonNode node = new ObjectMapper().readTree(jsonString);
        assertThat(extractTextOrNull(node, Arrays.asList("kakao_account", "email"))).isEqualTo("aliwo@naver.com");
    }

}
