---
title: ExpressJS
tags: ['javascript']
---


# NodeJS

## File System

```js
const fs = require('fs');

fs.readFile('demoFile.html', 'utf8', (err, data) => {
    console.log(data); // print byte array
});

// create file
fs.appendFile('text.txt', 'content', (err) => {
    if (err) throw err;
});

fs.open('myFile.txt', 'wr', (err, file) => {
    if (err) throw err; 
    console.log(file);
});

fs.writeFile('demoFile.html', 'content', (err) => {
    if (err) throw err;
});

fs.appendFile('demoFile.html', 'additional content', (err) => {
    if (err) throw err;
});

fs.unlink('demoFile.html', (err) => {
    if (err) throw err;
});

fs.rename('demoFile.html', 'newName.txt', (err) => {
    if (err) throw err;
});

```

## Event Emitter

```js
const events = require('events');
const emitter = new events.EventEmitter();

// can be placed at 2 different modules
emitter.on('firstEvent', (data) => {
    console.log(`First subscriber: ${data}`);
});
emitter.emit('firstEvent', 'return data');

// Kế thừa Event Emitter sử dụng util
const util = require('util');
function Dialog() {
    this.msg = "Hello";
}

util.inherits(dialog, events);
Dialog.prototype.inform = function() {
    console.log(this.msg);
    this.emit("OK");
}

let dialog = new Dialog();
dialog.on("hello", () => {
    console.log('Nhận được tin nhắn');
});

// Kế thừa Event Emitter sử dụng call và apply
const util = require('util');
function Dialog() {
    events.call(this);
    this.msg = "Hello";
}

util.inherits(dialog, events);
Dialog.prototype.inform = function() {
    console.log(this.msg);
    this.emit("OK");
}

let dialog = new Dialog();
dialog.on("hello", () => {
    console.log('Nhận được tin nhắn');
});

// Kế thừa Event Emitter sử dụng class
const util = require('util');
class Dialog extends events {
    constructor() {
        super();
        this.msg = "Hello";
    },
    inform(data) {
        console.log(`${this.msg}: ${data}`);
        this.emit("OK");
    }
}

let dialog = new Dialog();
dialog.on("hello", (data) => {
    console.log('Nhận được tin nhắn', data);
});


```

## Global Objects

```js
console.log(__dirname);
console.log(__filename);
console.log(process.env);

setTimeout(() => {
    console.log('Print after 1 second');
}, 1000);

setInterval(() => {
    console.log('Print for each 1 second');
}, 1000);

```

## Buffers

```js
const buff = new Buffer([10, 20]);
const buffer = new Buffer('data', 'utf-8');
console.log(buffer.toString('utf-8', 1, 4));

buffer.write('Hello');
```

## Typed Array

```js
let buffer = new ArrayBuffer(8);
let view = new Int32Array(buffer);
view[0] = 1;
view[1] = 1;
view[2] = 1; // chỉ có 8 byte
```

## Streams

```js
const fs = require('fs');
let data = '';

// stream itself is a event emitter
let readerStream = fs.createReadStream('input.txt');
readerStream.setEncoding('UTF8');

readerStream.on('data', (chunk) => {
    data += chunk;
});

readerStream.on('end', (chunk) => {
    console.log(data);
});

readerStream.on('error', (err) => {
    console.log(err.stack);
});


let data = 'Some data to write';

// stream itself is a event emitter
let writerStream = fs.createWriteStream('output.txt');
writerStream.write(data, 'UTF8');

writerStream.end();

writerStream.on('finish', () => {
    console.log('Write completed');
});

writerStream.on('error', (err) => {
    console.log(err.stack);
});


// Pipe Technique
readerStream.pipe(writerStream);

// Nén file
let zlib = require('zlib');
let gzip = zlib.createGzip();
let compressed = fs.createWriteStream('output.txt.gz');
readerStream.pipe(gzip).pipe(compressed);
```

## HTTP Module

### Simple Server

```js
const http = require("http");
const port = 3000;
const server = http.createServer((request, response) => {
    console.log(request.url);
    response.writeHead(200, {'Content-Type': 'text/plain'});
    response.write("Xin chào mấy nhóc");
    response.end('End');
});
server.listen(port);
```

### Static File Server

```js
const http = require("http");
const url = require("url");
const path = require("path");
const fs = require("fs");

const server = http.createServer((request, response) => {
    let requestedFile = url.parse(request.url).pathname;
    let filename = path.join(process.cwd(), requestedFile);

    fs.exists(filename, exist => {
        if (!exist) {
            response.writeHead(404, {"Content-Type": "text/html"});
            response.write("<h1>404 Error</h1>\n");
            response.write("The requested file isn't on this machine\n");
            response.end();
            return;
        } 

        fs.readFile(filename, "binary", (err, file) => {
            if (err) { 
                response.writeHead(500, {"Content-Type": "text/html"});
                response.write("<h1>500 Error</h1>\n"); 
                response.write(err + "\n");
                response.end();
                return;
            }
            response.writeHead(200);
            response.write(file, "binary");
            response.end();
        });
    });

});

const port = 3000;
server.listen(port, "localhost");
```

# ExpressJS

## Middleware

Some modules: body-parser, cookie-parser, multer
Some properties of Request object: app, baseurl, body, cookies, ip, params, query, xhr
Some properties of Request object: app, headersSent, locals
```js
const express = require("express");
const http = require('http');
const app = express();
const port = 3000;

// Middleware pineline
app.use((request, response, next) => {
    console.log(request.url);
    next();
});

// Authenticate middleware
app.use((request, response, next) => {
    let min = (new Date()).getMinutes();
    if (min % 2 == 0) 
    {
        next();
    } else {
        response.statusCode = 403;
        response.end('Login failed');
    }
});

app.use((request, response) => {
    // do something
});

http.createServer(app).listen(port);
```

```js
const express = require("express");
const path = require("path");
const app = express();
const options = {
    root: path.join(__dirname, "public");
};

app.get("/:filename", (request, response) => {
    response.sendFile(request.params.filename, options, (err) => {
        if (err) {
            console.log(err);
            response.status(404).send("File Not Found");
        }
        else {
            console.log("Sent: ", request.params.filename);
        }
    });
});

const port = 3000;
app.listen(port, () => {
    console.log("File server using express");
});
```

## Post Request 

```js

const express = require("express");
const app = express();

const bodyParser = require('body-parser');
const urlencodeParser = bodyParser.urlencoded({extended: false});

app.post("/product", urlencodeParser, (request, response) => {
    let userInfo = {
        username: request.body.username,
        password: request.body.pass
    };
    response.end(JSON.stringify(userInfo));
});

const port = 3000;
app.listen(port, () => {
    console.log("File server using express");
});
```

## Cookies

```js
const express = require("express");
const app = express();

const cookieParser = require('cookie-parser');
app.use(cookieParser());

app.get("/cookie", (request, response) => {
    response.cookie('cookie_key', 'cookie_value');
    response.send('Cookie was sent');
});

const port = 3000;
app.listen(port, () => {
    console.log("File server using express");
});
```

## Upload File

```js
const express = require('express');
const app = new express();

const multer = require('multer');
const storage = multer.diskStorage({
    destination: function(request, file, callback) {
        callback(null, './upload');
    }, 
    filename: function(request, file, callback) {
        callback(null, file.originalname);
    }
});

const upload = multer({storage: storage}).single('my_file');

app.get('/upload', (req, res) => {
    upload(req, res, (err) => {
        if (err) return res.end('error');
        res.end('File was successfully uploaded')
    });
});

app.listen(3000, () => {
    console.log('Server is running on port 3000');
});
```

# Template Engine

```js
function useEngine(app) {
    app.engine("html", (filePath, options, callback) => {
        fs.readFile(filePath, "utf-8", (err, content) => {
            if (err) return callback(err);

            
            let rendered = content.toString();
            for (const property in options) {
                if (options.hasOwnProperty(property) && property != 'settings' && property != '_locals' && property != 'cache') {
                    rendered = rendered.replace(`{{${camel2kebab(property)}}}`, options[property]);
                }
            }

            return callback(null, rendered);
        });
    });

    app.set("views", path.join(__dirname, "views"));
    app.set("view engine", "html");

    function camel2kebab(camel) {
        return camel.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
    }
}
```

# Enviroment Variables

npm install dotenv


```js
// filename.env
PORT=8080
BUILD=development
```

```js
require('dotenv').config();
console.log("build type=" + process.env.BUILD); 
server.listen(process.env.PORT);
```

# Postgre SQL

```js
const pgp = require('pg-promise')();

const cn = {
    host: 'localhost',
    port: 5432,
    database: 'my-database-name',
    user: 'user-name',
    password: 'user-password',
    max: 30 // use up to 30 connections

    // "types" - in case you want to set custom type parsers on the pool level
};

const db = pgp(cn);
```



