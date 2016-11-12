var express = require('express');
var router = express.Router();

var SELECT_QUERY = "select keyword as text,count(*) as count  from searches group by keyword order by count desc limit 0,10"
router.use('/', function(req, result) {


    mySQLpool.query(SELECT_QUERY, function(err, rows) {
        if(!err) {
            var returnObject = {
                "type": 1,
                "heading": "Trending",
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