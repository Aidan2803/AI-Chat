package com.ether.ai_chat_server;

import com.ether.ai_chat_server.client.AIClient;
import com.ether.ai_chat_server.client.FakeAIClient;
import com.ether.ai_chat_server.dto.ConversationResponse;
import com.ether.ai_chat_server.exceptions.ConversationNotFoundException;
import com.ether.ai_chat_server.model.ApplicationUser;
import com.ether.ai_chat_server.model.Conversation;
import com.ether.ai_chat_server.model.Message;
import com.ether.ai_chat_server.repository.ApplicationUserRepository;
import com.ether.ai_chat_server.repository.ConversationRepository;
import com.ether.ai_chat_server.repository.MessageRepository;
import com.ether.ai_chat_server.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiChatServerApplicationTests {

	private ChatService chatService;

	@Mock
	private FakeAIClient aiClient;

	@Mock
	private MessageRepository messageRepository;

	@Mock
	public ConversationRepository conversationRepository;

    @Mock
    public ApplicationUserRepository userRepository;

	@BeforeEach
	void setUp(){
		chatService = new ChatService(aiClient, conversationRepository, messageRepository, userRepository);
	}

	@Test
	void processMessageWorks() {
		String messageToProcess = "My message";

		ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);

		when(conversationRepository.findById(1L)).thenReturn(Optional.of(new Conversation()));
		when(aiClient.ask(any())).thenReturn("This is a stub ai response here.");

		String response = chatService.processMessage(messageToProcess, 1L);

		verify(aiClient, times(1)).ask(messageToProcess);
		verify(messageRepository, times(2)).save(messageArgumentCaptor.capture());

		List<Message> capturedMessage = messageArgumentCaptor.getAllValues();

		assertNotNull(response);

		assertEquals(messageToProcess, capturedMessage.get(0).getText());
		assertEquals(response, capturedMessage.get(1).getText());
	}

	@Test
	void processMessageThrowsWithoutConversation() {
		String messageToProcess = "My message";

		when(conversationRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ConversationNotFoundException.class, () -> chatService.processMessage(messageToProcess, 1L));

	}

	@Test
	void createConversationWorks() {
		String title = "New conversation";
        String username = "user1";
        String dummyHash = "dummyHash111";

        Conversation savedConversation = new Conversation();
        savedConversation.setTitle(title);

        ApplicationUser savedUser = new ApplicationUser(username, dummyHash);

		when(conversationRepository.save(any())).thenReturn(savedConversation);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(savedUser));

		Conversation conversation = chatService.createConversation(title, username);

		verify(conversationRepository, times(1)).save(any());
		assertEquals(title, conversation.getTitle());
	}

	@Test
	void getAllConversationsWorks(){
		Conversation c1 = new Conversation();
		c1.setTitle("Conversation1");
		Conversation c2 = new Conversation();
		c2.setTitle("Conversation2");
		Conversation c3 = new Conversation();
		c2.setTitle("Conversation3");

		when(conversationRepository.findAll()).thenReturn(List.of(c1, c2, c3));

		List<ConversationResponse> conversationResponses = chatService.getAllConversations();

		int expectedResultSize = 3;

		assertEquals(expectedResultSize, conversationResponses.size());
		assertEquals(c1.getTitle(), conversationResponses.get(0).title());
	}

	@Test
	void emptyGetAllConversation(){
		when(conversationRepository.findAll()).thenReturn(List.of());

		List<ConversationResponse> conversationResponses = chatService.getAllConversations();

		int expectedResultSize = 0;

		assertEquals(expectedResultSize, conversationResponses.size());
	}

	@Test
	void deleteConversationWorks(){
		when(conversationRepository.existsById(1L)).thenReturn(true);

		chatService.deleteConversation(1L);

		verify(conversationRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteConversationThrows(){
		when(conversationRepository.existsById(1L)).thenReturn(false);
		assertThrows(ConversationNotFoundException.class, () -> chatService.deleteConversation(1L));
	}
}
