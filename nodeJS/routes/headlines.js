var express = require('express');
var router = express.Router();
var dateFormat = require('dateformat');



router.use('/', function(req, result) {

    var today = dateFormat(Date(), "mmmm d yyyy");
    var SELECT_QUERY = "select * from news where `date` like '%"+ today + "%' order by id desc limit 0,20"



    mySQLpool.query(SELECT_QUERY, function(err, rows) {
        if(!err) {

            var values =[]
            values[0] = {
                      "date": dateFormat(Date(), "d mmm"),
                      "news": rows
                 }
            var returnObject = {
                "type": 2,
                "heading": "Headlines",
                "errorMessage": null,
                "values": values,
                "date": today
            }
            result.json(returnObject);
        }
        else
        {

            // console.log(INSERT_QUERY)
            result.json(err)
        }
    });

});

module.exports = router;