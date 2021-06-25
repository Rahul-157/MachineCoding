const express = require('express');
const socketIO = require('socket.io');
var app = express();
const logWriter = require('./LogWriter');
var io = socketIO(app.listen(3000));
const log_file = "logs.txt"
var child_process = require('child_process')
logWriter.startWriter();

// make connection with user from server side
io.on('connection', (socket) => {
  console.log('New user connected');
  let last_msg = ""
  setInterval(() => {
      var msg = child_process.spawnSync("tail",["-1",log_file]).stdout.toString()
      if(last_msg!=msg){
        socket.emit('newMessage', {
          text: msg.split('\r\n')
        });
        last_msg=msg;
      }
   
  }, 10)

});

app.use('/', (req, res) => {
  res.sendFile(__dirname + '/index.html');
});