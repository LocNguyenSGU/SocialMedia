package com.example.social.media.payload.request.SearchRequest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListSearchRequest extends ListRequest {
    String query;
}
