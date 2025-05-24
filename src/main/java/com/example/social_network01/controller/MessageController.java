package com.example.social_network01.controller;

import com.example.social_network01.dto.message.MessageDTO;
import com.example.social_network01.dto.message.MessageRequestDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats/{chatId}/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    @PreAuthorize("@chatService.isUserParticipant(#chatId, #currentUser.id)")
    public ResponseEntity<Page<MessageDTO>> getChatMessages(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdWhen,desc") String[] sort,
            @AuthenticationPrincipal User currentUser) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        return ResponseEntity.ok(messageService.getMessagesByChatId(chatId, pageable));
    }


//    @Operation(summary = "Create message with files",
//            description = "Create a new message in chat with optional file attachments")
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("@chatService.isUserParticipant(#chatId, #currentUser.id)")
//    public ResponseEntity<MessageDTO> createMessage(
//            @Parameter(description = "ID чата", required = true)
//            @PathVariable Long chatId,
//
//            @Parameter(description = "Message data and files",
//                    content = @Content(
//                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
//                            schema = @Schema(implementation = MessageRequestDTO.class),
//                            encoding = {
//                                    @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE),
//                                    @Encoding(name = "files", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//                            }
//                    ))
//            @Valid @RequestPart("request") MessageRequestDTO request,
//
//            @Parameter(description = "List of files to attach")
//            @RequestPart(value = "files", required = false) List<MultipartFile> files,
//
//            @AuthenticationPrincipal User currentUser) {
//
//        // Привязываем файлы к DTO если они есть
//        if (files != null && !files.isEmpty()) {
//            request.setFiles(files);
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(messageService.createMessage(
//                        chatId,
//                        currentUser.getId(),
//                        request
//                ));
//    }

    @PostMapping
    @PreAuthorize("@chatService.isUserParticipant(#chatId, #currentUser.id)")
    public ResponseEntity<MessageDTO> createMessage(
            @PathVariable Long chatId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания сообщения",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MessageRequestDTO.class),
                            encoding = @Encoding(
                                    name = "files",
                                    contentType = "application/octet-stream"
                            )
                    )
            )
            @Valid MessageRequestDTO request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.createMessage(chatId, currentUser.getId(), request));
    }

    @PutMapping("/{messageId}")
    @PreAuthorize("@messageService.isMessageAuthor(#messageId, #currentUser.id)")
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable Long chatId,
            @PathVariable Long messageId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания сообщения",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MessageRequestDTO.class),
                            encoding = @Encoding(
                                    name = "files",
                                    contentType = "application/octet-stream"
                            )
                    )
            )
            @Valid MessageRequestDTO request,
            @AuthenticationPrincipal User currentUser) {

        MessageDTO updatedMessage = messageService.updateMessage(
                messageId,
                currentUser.getId(),
                request
        );
        return ResponseEntity.ok(updatedMessage);
    }

    @GetMapping("/{messageId}")
    @PreAuthorize("@chatService.isUserParticipant(#chatId, #currentUser.id)")
    public ResponseEntity<MessageDTO> getMessage(
            @PathVariable Long chatId,
            @PathVariable Long messageId,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @DeleteMapping("/{messageId}")
    @PreAuthorize("@messageService.isMessageAuthor(#messageId, #currentUser.id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long chatId,
            @PathVariable Long messageId,
            @AuthenticationPrincipal User currentUser) {

        messageService.deleteMessage(chatId, messageId);
        return ResponseEntity.noContent().build();
    }

}