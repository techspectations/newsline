<<<<<<< HEAD
plingd.com
=======
const express = require('express')
const app = express();
//var compression = require('compression');
//app.use(compression());

const PORT = 80;
var mysql     =    require('mysql');
bodyParser = require('body-parser')
require('./constants.js')
//const NodeCache = require( "node-cache" );
//myCache = new NodeCache( { stdTTL: 86400, checkperiod: 86400 } );


app.use(bodyParser.json());       // to support JSON-encoded bodies
//app.use(helmet());





// Creating mysql connection pool
mySQLpool  =    mysql.createPool({
    connectionLimit : 150, //important
    host     : 'localhost',
    user     : 'root',
    password : 'tech@123',
    database : 'news',
    debug    :  false
});



// initiating the routes
app.use('/api/populate', require('./routes/populate'));
app.use('/api/Categories', require('./routes/categories'));
app.use('/api/Headlines', require('./routes/headlines'));
app.use('/api/Trending', require('./routes/trending'));
app.use('/api/news', require('./routes/news'));
app.use('/api', require('./routes/search'));





// starting server on port 80
app.listen(PORT);

>>>>>>> origin/master
