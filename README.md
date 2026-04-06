# Distributed File Server 

## Overview

This project is a multithreaded TCP-based Distributed File Server developed in Java. It allows multiple clients to upload and download files concurrently, simulating the core functionality of cloud storage systems like Google Drive.

---

##  Features

*  Upload files from client to server
*  Download files from server to client
*  Concurrent client handling using multithreading
*  Centralized file storage on server
*  Reliable communication using TCP protocol

---

##  Technologies Used

* Java (Core Java, Sockets, Multithreading)
* TCP Networking (`ServerSocket`, `Socket`)
* File I/O (`FileInputStream`, `FileOutputStream`)

---

##  How It Works

1. The server starts and listens for incoming client connections.
2. Each client connection is handled using a separate thread.
3. Clients can send commands:

   * `UPLOAD` → Send file to server
   * `DOWNLOAD` → Request file from server
4. The server processes requests and responds accordingly.

---

##  Project Structure

```
Distributed-File-Server/
│
├── FileServer.java        # Server-side code
├── FileClient.java        # Client-side code
├── server_files/          # Stored files on server(created after you compile server) 
└── README.md
```

---

## ⚙️ How to Run

### 1. Compile

```
javac FileServer.java
javac FileClient.java
```

### 2. Start Server

```
java FileServer
```

### 3. Run Client(in separate cmd)

```
java FileClient
```

---

##  Usage

### Upload File

* Enter the file path(name of file) . File has to be stored in program folder.
* File will be stored in `server_files/` directory

### Download File

* Enter the **file name**
* File will be saved as `downloaded_<filename>`

---

## ⚠️ Limitations

* No authentication system
* No encryption for file transfer
* No file listing feature
* No resume support for interrupted transfers

---

## Learning Outcome

This project demonstrates how to build a scalable concurrent server capable of handling multiple clients and performing reliable file transfers using TCP.

---
