var express = require('express');
var router = express.Router();


var SELECT_QUERY = "select *  from news where id =?"
router.use('/:id', function(req, result) {

    var newsID = req.params.id
    mySQLpool.query(SELECT_QUERY,[newsID], function(err, rows) {
        if(!err) {

           result.json(rows[0]);

        }
        else
        {

            // console.log(INSERT_QUERY)
            result.json(err)
        }
    });


    //todo: add searches to search table for trending items

});

module.exports = router;