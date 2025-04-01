package com.example.social.media.payload.request.MessageDTO;

public class CallRequest {
    private String callerId;
    private String callerName;
    private String receiverId;
    private String roomId;

    // Constructors, Getters, Setters

    public CallRequest() {
    }

    public CallRequest(String callerId, String callerName, String receiverId, String roomId) {
        this.callerId = callerId;
        this.callerName = callerName;
        this.receiverId = receiverId;
        this.roomId = roomId;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}

