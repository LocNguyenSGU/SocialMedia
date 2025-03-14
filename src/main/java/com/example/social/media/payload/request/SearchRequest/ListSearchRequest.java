package com.example.social.media.payload.request.SearchRequest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListSearchRequest {
    int page = 1;
    int pageSize = 10;
    String sort;
    String query;
}
