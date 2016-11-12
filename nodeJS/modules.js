var exports = module.exports = {};
var https = require('https');
var reqGet = []

var INSERT_QUERY = "INSERT INTO news (title,thumbnail,imgMob,date,avgRating,category,content) VALUES ?";

exports.getData =    function(result,term,pageNo,category)
{
    var searchPath = '/api/editions/en/search?type=all&term='+term+"&page="+pageNo

    console.log(searchPath)
    var optionsget = {
        host : 'developer.manoramaonline.com',
        path : searchPath,
        method : 'GET' ,
        headers : {'Authorization' : 'a54d36e2-33b0-5396-b84d-b76ed415fff3'}
    }

    reqGet[pageNo] = https.request(optionsget, function(res) {

        var retVal = "";
        res.on('data', function(d) {
            retVal = retVal + d
        });

        res.on('end', function() {
            var newsItems =[]
            var jsonResult
            var articles
            try {
                jsonResult = JSON.parse(retVal);
                articles = jsonResult.articles

                for(i=0;i<articles.length;i++)
                {
                    var newsItem = []
                    newsItem[0] =  articles[i].title
                    newsItem[1] =  articles[i].thumbnail
                    newsItem[2] =  articles[i].imgMob
                    newsItem[3] =  articles[i].lastModified
                    newsItem[4] = 0
                    newsItem[5] =  category
                    newsItem[6] =  articles[i].title
                    newsItems.push(newsItem)
                }
            } catch(e) {
                result.json('No Result');
            }

                mySQLpool.query(INSERT_QUERY,[newsItems], function(err, rows) {
                    if(!err) {
                        result.json("completed")
                    }
                    else
                    {

                       // console.log(INSERT_QUERY)
                        result.json('No Result');
                    }
                });


                 //   newsItems[articles[i].articleID] =   newsItem

                    /*
                     var articlePath = "/api/editions/en/articles/"+articles[i].articleID
                     var optionsget = {
                        host : 'developer.manoramaonline.com',
                        path : articlePath,
                        method : 'GET' ,
                        headers : {'Authorization' : 'a54d36e2-33b0-5396-b84d-b76ed415fff3'}
                     }

                     reqGet[articles[i].articleID] = https.request(optionsget, function(res) {
                         var retVal = "";
                         res.on('data', function(d) {
                                 retVal = retVal + d
                         });
                         res.on('end', function() {
                             try {
                                 jsonResult = JSON.parse(retVal);
                                 newsItems[jsonResult.articleID][6]    = jsonResult.content

                                 if(jsonResult.content == null || jsonResult.content == "undefined")
                                     newsItems[jsonResult.articleID][6]    = ""
                                      console.log(newsItems)
                                 mySQLpool.query(INSERT_QUERY,[newsItems], function(err, rows) {
                                     if(!err) {

                                     }
                                     else
                                     {

                                         console.log("insert error")
                                     }
                                 });
                                 return
                             } catch(e) {
                                console.log('malformed request article ');
                             }
                         });
                     });

                     reqGet[articles[i].articleID].end();
                     reqGet[articles[i].articleID].on('error', function(e) {
                     console.error(e);
                     });

                     */






              //  console.log(newsItems)
           // return
        });
    });

    reqGet[pageNo].end();
    reqGet[pageNo].on('error', function(e) {
        console.error(e);
    });
}



