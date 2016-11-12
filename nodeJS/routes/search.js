var express = require('express');
var router = express.Router();
var dateFormat = require('dateformat');


var SELECT_QUERY = "select category  from news_category"
var QUERY

var monthNames = ["jan","january","feb","february","mar","march","apr","april","may","may","jun","june","jul","july","aug","august","sep","september","oct","october","nov","november","dec","december"]



router.use('/:keyword', function(req, result) {
    var forMonth = 0
    var forYear = 0
    var toGroup = 1
    var keyword = req.params.keyword
    var keywordArray = keyword.split(" ")

    console.log("searching for"+keyword )

    for(var i=0;i<keywordArray.length;i++)
    {
        for(var j=0;j<monthNames.length;j++)
        {
            if(monthNames[j] == keywordArray[i])
            {
                forMonth = monthNames[j]
            }
        }
        if(parseInt(keywordArray[i]) > 1900 && parseInt(keywordArray[i]) < 2017)
        {
            forYear = keywordArray[i]
        }
    }



    if(forYear != 0)
        keyword = keyword.replace(forYear, "");

    if(forMonth != 0)
        keyword = keyword.replace(forMonth, "");

    keyword = keyword.replace("  ", " ");
    keyword = keyword.trim()


    if(forMonth != 0)
       toGroup = 0


    var isCategory = false

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
               QUERY =  "SELECT n.* FROM news n inner join news_category nc on nc.id = n.category where nc.category like '" + keyword + "'"
            else
               QUERY = "SELECT * FROM news where title like '%" + keyword + "%'"

            if(forYear != 0)
                QUERY = QUERY + " and `date` like '%" + forYear + "%'"

            if(forMonth != 0)
                QUERY = QUERY + " and `date` like '%" + forMonth + "%'"


            QUERY = QUERY +" order by date_new"
            var dateform =  "yyyy mmm"

            mySQLpool.query(QUERY,function(err, rows) {
                if(!err) {

                    var values =[]
                    var k =-1
                    var processedDate
                    for(var i=0;i<rows.length;i++)
                    {
                        if(processedDate!=dateFormat(rows[i].date, "yyyy mmm"))
                        {
                            processedDate = dateFormat(rows[i].date, "yyyy mmm")
                            k = k + 1
                            values[k] = {
                                "date": processedDate,
                                "news": []
                            }
                            values[k]["news"].push(rows[i])
                        }
                        else
                        {
                            values[k]["news"].push(rows[i])
                        }

                    }


                    var returnObject = {
                        "type": 2,
                        "heading": keyword,
                        "errorMessage": null,
                        "values": values,
                        "date": dateFormat(Date(), "yyyy mmm d"),
                        "toGroup":toGroup
                    }
                    result.json(returnObject);
                    //result.json(QUERY)
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