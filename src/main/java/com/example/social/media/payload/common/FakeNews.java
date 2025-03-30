package com.example.social.media.payload.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FakeNews {
    String fakeNewsSign;
    String reason;
}
