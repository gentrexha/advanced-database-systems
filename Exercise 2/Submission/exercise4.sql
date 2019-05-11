--Create database
create database group11;

--Create table badges

CREATE EXTERNAL TABLE IF NOT EXISTS group11.badges (
id INT,class STRING, date DATE, name STRING, tagbased STRING, userid INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/home/adbs/2019S/shared/hive/badges.csv';


-- create table comments
CREATE EXTERNAL TABLE IF NOT EXISTS group11.comments (
id INT,creationdate DATE, postid INT, score INT, text STRING, userdisplayname STRING,userid INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/home/adbs/2019S/shared/hive/comments.csv';


-- create table posts
CREATE EXTERNAL TABLE IF NOT EXISTS group11.posts (
id INT,acceptedanswerid INT, answercount INT, body STRING, closeddate DATE, commentcount INT,communityowneddate DATE, creationdate DATE, favoritecount INT, lastactivitydate DATE, lasteditdate DATE, lasteditordisplayname STRING, lasteditoruserid INT, ownerdisplayname STRING, owneruserid INT,parentid INT,posttypeid INT,score INT,tags STRING,title STRING,viewcount INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/home/adbs/2019S/shared/hive/posts.csv';

-- create table postslinks
CREATE EXTERNAL TABLE IF NOT EXISTS group11.postlinks (
id INT,creationdate DATE,linktypeid INT,postid INT,relatedpostid INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/home/adbs/2019S/shared/hive/postlinks.csv';


--create table users
CREATE EXTERNAL TABLE IF NOT EXISTS group11.users (
id INT,aboutme STRING,accountid INT,creationdate DATE,displayname STRING,downvotes INT,lastaccessdate DATE,location STRING,profileimageurl STRING,reputation INT,upvotes INT,views INT,websiteurl STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/home/adbs/2019S/shared/hive/users.csv';


--create table votes
CREATE EXTERNAL TABLE IF NOT EXISTS group11.votes (
id INT,bountyamount INT,creationdate DATE,postid INT,userid INT,votetypeid INT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/home/adbs/2019S/shared/hive/votes.csv';

