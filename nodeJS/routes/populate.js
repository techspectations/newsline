var express = require('express');
var router = express.Router();
var modules = require('../modules.js')
var https = require('https');
var async = require('async');


router.post('/', function(req, result) {

    //var a = modules.getData("trump",1)
   // res.send(a)

    var newsObjects = []
    var term = req.body.term
    var pageNo = parseInt(req.body.pageNo)
    var category = parseInt(req.body.category)

    modules.getData(result,term,pageNo,category)

    /*
    var retVal = ""
    var searchPath = '/api/editions/en/search?type=all&term='+term+"&page="+pageNo
    var optionsget = {
        host : 'developer.manoramaonline.com',
        path : searchPath,
        method : 'GET' ,
        headers : {'Authorization' : 'a54d36e2-33b0-5396-b84d-b76ed415fff3'}
    }

    var reqGet = https.request(optionsget, function(res) {
        res.on('data', function(d) {
            retVal = retVal + d
        });

        res.on('end', function() {
           // var jsonResult = JSON.parse(retVal).articles;
                var pageCount = JSON.parse(retVal).totalPages
                    pageKeys =[]
            for(i=1;i<=pageCount;i++)
                pageKeys[i] = i

            async.forEach(Object.keys(pageKeys), function (item, callback){
                modules.getData("trump",parseInt(item),category)
                callback(); // tell async that the iterator has completed

            }, function(err) {
                result.send('iterating done');
            });
        });
    });

    reqGet.end();
    reqGet.on('error', function(e) {
        console.error(e);
    });

      */

});

module.exports = router;