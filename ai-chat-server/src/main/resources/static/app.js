const state = {
  conversations: [],
  selectedConversationId: null,
  selectedConversationTitle: null,
};

const elements = {
  conversationList: document.getElementById("conversation-list"),
  createForm: document.getElementById("create-conversation-form"),
  conversationTitleInput: document.getElementById("conversation-title"),
  chatTitle: document.getElementById("chat-title"),
  messages: document.getElementById("messages"),
  sendForm: document.getElementById("send-form"),
  messageInput: document.getElementById("message-input"),
  sendButton: document.getElementById("send-btn"),
  deleteConversationButton: document.getElementById("delete-conversation-btn"),
  status: document.getElementById("status"),
};

function setStatus(message = "") {
  elements.status.textContent = message;
}

async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Request failed with ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

function setConversationControlsEnabled(isEnabled) {
  elements.messageInput.disabled = !isEnabled;
  elements.sendButton.disabled = !isEnabled;
  elements.deleteConversationButton.disabled = !isEnabled;
}

function renderConversations() {
  elements.conversationList.innerHTML = "";

  if (state.conversations.length === 0) {
    const empty = document.createElement("li");
    empty.textContent = "No conversations yet.";
    elements.conversationList.appendChild(empty);
    return;
  }

  state.conversations.forEach((conversation) => {
    const item = document.createElement("li");
    item.className = "conversation-item";
    if (conversation.conversationID === state.selectedConversationId) {
      item.classList.add("active");
    }

    item.textContent = `${conversation.conversationID}: ${conversation.title}`;
    item.addEventListener("click", () => {
      selectConversation(conversation.conversationID, conversation.title);
    });

    elements.conversationList.appendChild(item);
  });
}

function renderMessages(messages) {
  elements.messages.innerHTML = "";

  if (messages.length === 0) {
    elements.messages.textContent = "No messages yet. Send one to start the chat.";
    return;
  }

  messages.forEach((message, index) => {
    const role = index % 2 === 0 ? "user" : "ai";
    const wrapper = document.createElement("article");
    wrapper.className = `message ${role}`;

    const meta = document.createElement("span");
    meta.className = "meta";
    meta.textContent = role.toUpperCase();

    const text = document.createElement("div");
    text.textContent = message.text;

    wrapper.appendChild(meta);
    wrapper.appendChild(text);
    elements.messages.appendChild(wrapper);
  });

  elements.messages.scrollTop = elements.messages.scrollHeight;
}

async function loadConversations() {
  setStatus("");
  state.conversations = await request("/chat/conversations");
  renderConversations();
}

async function loadConversationHistory(conversationId) {
  const messages = await request(`/chat/conversation/${conversationId}`);
  renderMessages(messages);
}

async function selectConversation(conversationId, title) {
  state.selectedConversationId = conversationId;
  state.selectedConversationTitle = title;
  elements.chatTitle.textContent = `${conversationId}: ${title}`;
  setConversationControlsEnabled(true);
  renderConversations();
  await loadConversationHistory(conversationId);
}

elements.createForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setStatus("");

  const title = elements.conversationTitleInput.value.trim();
  if (!title) {
    setStatus("Conversation title is required.");
    return;
  }

  try {
    const conversation = await request("/chat/conversation/create", {
      method: "POST",
      body: JSON.stringify({ title }),
    });

    elements.conversationTitleInput.value = "";
    await loadConversations();
    await selectConversation(conversation.conversationID, conversation.title);
  } catch (error) {
    setStatus(`Failed to create conversation: ${error.message}`);
  }
});

elements.sendForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setStatus("");

  if (!state.selectedConversationId) {
    setStatus("Create or select a conversation first.");
    return;
  }

  const message = elements.messageInput.value.trim();
  if (!message) {
    return;
  }

  elements.sendButton.disabled = true;

  try {
    await request(`/chat/conversation/${state.selectedConversationId}/sendMessage`, {
      method: "POST",
      body: JSON.stringify({ message }),
    });

    elements.messageInput.value = "";
    await loadConversationHistory(state.selectedConversationId);
  } catch (error) {
    setStatus(`Failed to send message: ${error.message}`);
  } finally {
    elements.sendButton.disabled = false;
  }
});

elements.deleteConversationButton.addEventListener("click", async () => {
  if (!state.selectedConversationId) {
    return;
  }

  const shouldDelete = window.confirm("Delete this conversation and all messages?");
  if (!shouldDelete) {
    return;
  }

  setStatus("");
  const idToDelete = state.selectedConversationId;

  try {
    await request(`/chat/conversation/${idToDelete}/delete`, { method: "DELETE" });

    state.selectedConversationId = null;
    state.selectedConversationTitle = null;
    elements.chatTitle.textContent = "Select a conversation";
    elements.messages.innerHTML = "";
    setConversationControlsEnabled(false);
    await loadConversations();
  } catch (error) {
    setStatus(`Failed to delete conversation: ${error.message}`);
  }
});

async function init() {
  setConversationControlsEnabled(false);
  elements.messages.textContent = "Create or select a conversation to begin.";

  try {
    await loadConversations();
  } catch (error) {
    setStatus(`Failed to load conversations: ${error.message}`);
  }
}

init();
