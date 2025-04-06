package com.example.social.media.payload.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage<T> implements Serializable {
    private int idReceiver;
    private T object;
}
