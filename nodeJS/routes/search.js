var express = require('express');
var router = express.Router();
var dateFormat = require('dateformat');


var SELECT_QUERY = "select category  from news_category"
var CATEGORY_QUERY = "SELECT n.* FROM news n inner join news_category nc on nc.id = n.category where nc.category =?"
var SEARCH_QUERY = "SELECT * FROM news where title like '%?%'"
var QUERY
router.post('/:keyword', function(req, result) {

    var isCategory = false
      // result.json(req.params.keyword)
    var keyword = req.params.keyword
    mySQLpool.query(SELECT_QUERY, function(err, rows) {
        if(!err) {
            for(var i=0;i<rows.length;i++)
            {
                if(keyword==rows[i].category)
                {
                    isCategory = true
                }
            }

            if(isCategory==true)
               QUERY = CATEGORY_QUERY
            else
               QUERY = SEARCH_QUERY


            mySQLpool.query(QUERY,[keyword],function(err, rows) {
                if(!err) {

                    var values =[]
                    values[0] = {
                        "date": dateFormat(Date(), "d mmm"),
                        "news": rows
                    }
                    var returnObject = {
                        "type": 2,
                        "heading": keyword,
                        "errorMessage": null,
                        "values": values,
                        "date": dateFormat(Date(), "d mmm")
                    }
                    result.json(returnObject);
                }
                else
                {

                    // console.log(INSERT_QUERY)
                    result.json(err)
                }
            });



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