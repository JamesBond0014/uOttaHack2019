'use strict';
const log = console.log;

const express = require('express')
const port = process.env.PORT || 3000
const bodyParser = require('body-parser') // middleware for parsing HTTP body from client
const session = require('express-session')
const fs = require('fs')
// express
const app = express();
// body-parser middleware setup.  Will parse the JSON and convert to object
app.use(bodyParser.json());
// parse incoming parameters to req.body
app.use(bodyParser.urlencoded({ extended:true }))

app.use("/json", express.static(__dirname + '/'))

let test
fs.readFile("parsedWizard2.txt",'utf8',(err,data)=>{
  if (err) throw error;
  test = data.split("UNIQUESTRINGSPLIT")
  app.listen(port, ()=>{
    log(`Listening on port ${port}...`)
  })
})



app.get('/:keywordsSteph',(req,res) =>{
  let keywords = req.params.keywordsSteph.split(',');

  if(keywords.length <=0){
    res.status(404).send()
  }
  let returnVal
  for (let i = 0;i<keywords.length;i++){
    returnVal = test.filter((a)=>{
      return (a.keywords.search(keywords[i])>=0)
    })
    console.log(returnVal);
    let r2
    if (returnVal.length >0){
        if (returnVal.length >2){
          r2 = {stuff:returnVal.slice(0,2)}
        } else {
          r2 ={stuff:returnVal}
        }
        res.send(r2)
        break
    }
  }
  res.status(400).send()
})
