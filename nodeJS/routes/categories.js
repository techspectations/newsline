var express = require('express');
var router = express.Router();

var SELECT_QUERY = "select id,category as text from news_category"
router.use('/', function(req, result) {


    mySQLpool.query(SELECT_QUERY, function(err, rows) {
        if(!err) {

            var returnObject = {
                "type": 1,
                "heading": "Categories",
                "errorMessage": null,
                "values": rows
            }
            result.json(returnObject);


        }
        else
        {

            // console.log(INSERT_QUERY)

            console.log(err)
        }
    });

});

module.exports = router;