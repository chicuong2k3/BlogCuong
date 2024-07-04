
# Introduction to SignalR

Today, many types of applications require real-time communication capabilities. For example, messaging applications need to display messages immediately after someone sends them. There are several ways to achieve this requirement, including Long Polling, Server-sent Events, and WebSocket.

## Long Polling

First, let's talk about Short Polling. Short Polling and Long Polling are similar in that they both use the HTTP protocol. With Short Polling, the client sends a request to the server to fetch data. If the server doesn't have any data yet, it responds with an empty response. Then, the client closes the connection and sends another request, repeating this process continuously. This is not efficient because it creates many HTTP connections.

In contrast to Short Polling, Long Polling sends a request and keeps the connection open until the server has data or until a timeout occurs (which is much longer than Short Polling), then it closes the connection and sends the response to the client, followed by creating a new connection. Long Polling has some drawbacks, such as consuming more bandwidth and server resources when there are too many connections to the server.

## Server-send Events

## WebSocket

Unlike the methods above, WebSocket directly uses the TCP protocol to establish a two-way connection between the client and the server (full duplex). Unlike Long Polling, WebSocket only needs to establish a connection once.

> [!NOTE]
> **Priority order of usage:** WebSocket $\rightarrow$ Server-send Events $\rightarrow$  Long Polling

## Some Usecases of SignalR

- High-throughput data updating applications such as gaming, voting, auctions...
- Dashboards and monitoring.
- Chat applications.
- Real-time location tracking applications.
- Collaborative apps.
- Push notifications such as social networks, emails...
- Real-time broadcasting such as livestreams, news broadcasts...
- IoT applications.