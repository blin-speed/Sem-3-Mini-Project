import React, { useState, useRef, useEffect } from "react";
import "./HealthChat.css";

function HealthChat() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const sessionId = sessionStorage.getItem("sessionId"); 
  const chatEndRef = useRef(null);

  const sendMessage = async () => {
    if (!input.trim()) return;

    const newMessages = [...messages, { sender: "user", text: input }];
    setMessages(newMessages);

    try {
      const response = await fetch(
        `http://localhost:8080/api/healthchat/${sessionId}/ask?userPrompt=${encodeURIComponent(
          input
        )}`
      );

      if (!response.ok) throw new Error("Server error");

      const data = await response.text();
      setMessages([...newMessages, { sender: "bot", text: data }]);
    } catch (error) {
      setMessages([
        ...newMessages,
        { sender: "bot", text: "⚠️ Error connecting to server" },
      ]);
    }

    setInput("");
  };

  // Auto-scroll to bottom
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="healthchat-container">
      {/* Header at top */}
      <header className="healthchat-header">💬 HealthChat</header>

      {/* Chat window fills middle */}
      <main className="healthchat-window">
        {messages.map((msg, idx) => (
          <div
            key={idx}
            className={`chat-message ${msg.sender === "user" ? "user" : "bot"}`}
          >
            <span className="chat-bubble">{msg.text}</span>
          </div>
        ))}
        <div ref={chatEndRef} />
      </main>

      {/* Input bar anchored at bottom */}
      <footer className="chat-input-container">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          className="chat-input"
          placeholder="Type your health question..."
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button onClick={sendMessage} className="chat-button">
          Send
        </button>
      </footer>
    </div>
  );
}

export default HealthChat;