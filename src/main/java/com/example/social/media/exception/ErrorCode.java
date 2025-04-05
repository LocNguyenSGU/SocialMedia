package com.example.social.media.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(1000 , "User not found" , HttpStatus.NOT_FOUND),
    USER_EXISTED(1001 , "User existed", HttpStatus.BAD_REQUEST),
    INVALID_USER_DATA(1002 , "User data is invalid" , HttpStatus.BAD_REQUEST),
    MESSAGE_NOT_EXITED(2000 , "Message not found" , HttpStatus.NOT_FOUND),
    MESSAGE_NOT_FOUND(2001, "Message not found", HttpStatus.NOT_FOUND),
    MESSAGE_SEND_FAILED(2002, "Failed to send message", HttpStatus.INTERNAL_SERVER_ERROR),
    MESSAGE_EMPTY(2003, "Message content cannot be empty", HttpStatus.BAD_REQUEST),
    MESSAGE_DELETE_FORBIDDEN(2004, "You do not have permission to delete this message", HttpStatus.FORBIDDEN),
    POST_NOT_FOUND(3000, "Post not found", HttpStatus.NOT_FOUND),
    POST_EXISTED(3001, "Post already exists", HttpStatus.BAD_REQUEST),
    POST_DELETE_FORBIDDEN(3002, "You do not have permission to delete this post", HttpStatus.FORBIDDEN),
    POST_INVALID_CONTENT(3003, "Post content is invalid", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND(4000, "Comment not found", HttpStatus.NOT_FOUND),
    COMMENT_DELETE_FORBIDDEN(4001, "You do not have permission to delete this comment", HttpStatus.FORBIDDEN),
    COMMENT_EMPTY(4002, "Comment cannot be empty", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_NOT_FOUND(5000, "Friend request not found", HttpStatus.NOT_FOUND),
    ALREADY_FRIENDS(5001, "Users are already friends", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_FRIEND_REQUEST(5002, "Cannot send friend request", HttpStatus.BAD_REQUEST),
    CANNOT_BLOCK_FRIEND(5003, "Cannot block this user", HttpStatus.FORBIDDEN),
    NOTIFICATION_NOT_FOUND(6000, "Notification not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_ACCESS_DENIED(6001, "You do not have permission to view this notification", HttpStatus.FORBIDDEN),
    CONVERSATION_NOT_FOUND(7000, "Conversation not found", HttpStatus.NOT_FOUND),
    CANNOT_ADD_USER_TO_CONVERSATION(7001, "Cannot add user to conversation", HttpStatus.BAD_REQUEST),
    CONVERSATION_ACCESS_DENIED(7002, "You do not have permission to access this conversation", HttpStatus.FORBIDDEN),
    MEDIA_NOT_FOUND(8000, "Media not found", HttpStatus.NOT_FOUND),
    MEDIA_UPLOAD_FAILED(8001, "Failed to upload media", HttpStatus.INTERNAL_SERVER_ERROR),
    MEDIA_FORMAT_UNSUPPORTED(8002, "Media format is not supported", HttpStatus.BAD_REQUEST),
    POST_NOT_EXISTED(8000 , "Post not found" , HttpStatus.NOT_FOUND),
    BAD_REQUEST_FAKE_NEWS(8002, "Post khong co content o dang chu, khong kiem tra duoc",HttpStatus.BAD_REQUEST),
    POST_MEDIA_NOT_FOUND(10000, "Post media not found", HttpStatus.NOT_FOUND),
    POST_EMOTION_NOT_FOUND(11000, "Post emotion not found", HttpStatus.NOT_FOUND),
    ;


    int code;
    String message;
    HttpStatusCode statusCode;
}
