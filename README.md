## Overview
 
This is a desktop-based chat application where multiple clients can connect to a central server and communicate in real time. The server handles all incoming connections, routes messages between clients, and maintains an up-to-date list of online users.

## ✨ Feature
 
- 🔐 User Registration and Login
- 👥 Live online user list
- 🔌 Multi-client server using multithreading

## 🏗️ Architecture
 
The application follows a **Client-Server architecture** over raw **TCP sockets**.
 
```
┌─────────────────────────────────────┐
│              CLIENT SIDE            │
│                                     │
│  JavaFX UI  ←→  ChatClientService  │
│  (FXML Views + Controllers)         │
└────────────────┬────────────────────┘
                 │ TCP Socket (Port 5000)
                 │ ObjectInputStream / ObjectOutputStream
┌────────────────▼────────────────────┐
│              SERVER SIDE            │
│                                     │
│  ChatServer  →  ClientHandler(s)   │
│  (Singleton)    (One per client,    │
│                  runs on own thread)│
└─────────────────────────────────────┘
```
 
**Message Flow:**
1. Client serializes a `Message` object and sends it over the socket
2. Server's `ClientHandler` deserializes it and routes based on `Message.Type`
3. Server broadcasts or routes the message to the appropriate client(s)
4. Client receives the message and updates the JavaFX UI via `Platform.runLater()`
---
 
## 🎨 Design Patterns
 
| Pattern | Where Used | Purpose |
|---------|-----------|---------|
| **Singleton** | `ChatServer`, `ChatClientService`, `UserData` | Single instance across the application |
| **Observer** | `ChatClientService` → `ChatController` via `Consumer<Message>` | Decouple message receiving from UI updates |
| **Factory Method** | `ClientHandlerFactory.createHandler()` | Encapsulate `ClientHandler` creation |
| **MVC** | Controllers / Models / FXML Views | Separate UI logic from business logic |
 
---

## 🛠️ Technologies Used
 
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Core language |
| JavaFX | 17+ | Desktop UI framework |
| Java TCP Sockets | - | Client-server communication |
| Java Serialization | - | Object streaming over sockets |
| Maven | 3.x | Dependency management & build |

## 🚀 How to Run
 
### Step 1 — Clone the repository
 
```bash
git clone https://github.com/your-username/chatapp.git
cd chatapp
```
 
### Step 2 — Start the Server
 
Run the `main()` method inside `ChatServer.java`:
 
```bash
# In IntelliJ: Right-click ChatServer.java → Run 'ChatServer.main()'
```
 
You should see:
```
Server Started
```
 
### Step 3 — Start the Client(s)
 
Run the `main()` method inside `Launcher.java`:
 
```bash
# In IntelliJ: Right-click Launcher.java → Run 'Launcher.main()'
```
 
> 💡 You can launch multiple client instances to simulate multiple users chatting.
 
### Step 4 — Register and Login
 
1. On the landing page, click **Register**
2. Fill in Name, Username, and Password → click Register
3. Click **If you have an account** → go to Login
4. Enter credentials → click Login
5. Start chatting! 🎉
---
