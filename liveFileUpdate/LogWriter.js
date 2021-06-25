var fs = require('fs');
var uuid = require("uuid");


module.exports.startWriter =  function startWriter() {
    setInterval(() => {
        var id = uuid.v4();
        console.log(id)
        fs.appendFile('logs.txt', id+"\r\n", (err) => {
            if(err)
            throw err;
        });
        
    }, Math.random()*2000);
}