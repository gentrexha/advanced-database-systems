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

--b
--Placeholder
CREATE TABLE badges_p (id INT, class INT, timestamp STRING, name STRING, tagbased BOOLEAN) PARTITIONED BY (userid INT);
INSERT OVERWRITE TABLE badges_p PARTITION (userid) SELECT id, class, timestamp, name, tagbased, userid FROM badges;

--Placeholder
CREATE TABLE users_p (id INT, aboutme STRING, accountid INT, creationdate STRING, displayname STRING, downvotes INT, lastaccessdate STRING, location STRING, profileimageurl STRING, upvotes INT, views INT, websiteurl STRING) PARTITIONED BY (reputation INT);
INSERT OVERWRITE TABLE users_p PARTITION (reputation) SELECT id, aboutme, accountid, creationdate, displayname, downvotes, lastaccessdate, location, profileimageurl, upvotes, views, websiteurl, reputation FROM users;

--Placeholder
CREATE TABLE users_b (id INT, aboutme STRING, accountid INT, creationdate STRING, displayname STRING, downvotes INT, lastaccessdate STRING, location STRING, profileimageurl STRING, upvotes INT, views INT, websiteurl STRING, reputation INT) CLUSTERED BY (id) INTO 32 BUCKETS;
INSERT OVERWRITE TABLE users_b SELECT id, aboutme, accountid, creationdate, displayname, downvotes, lastaccessdate, location, profileimageurl, upvotes, views, websiteurl, reputation FROM users;

--Placeholder
CREATE TABLE comments_b (id INT, creationdate STRING, postid INT, score INT, text STRING, userdisplayname STRING, userid INT) CLUSTERED BY (id) INTO 32 BUCKETS;
INSERT OVERWRITE TABLE comments_b SELECT id, creationdate, postid, score, text, userdisplayname, userid FROM comments;

--Placeholder
SELECT p.id FROM posts p, comments c, users u, votes v
WHERE c.postid=p.id AND c.userid=p.owneruserid AND u.id=p.owneruserid
AND u.reputation > 100 AND v.postid = p.id AND v.userid = p.owneruserid
AND NOT EXISTS (SELECT 1 FROM postlinks l WHERE l.relatedpostid = p.id);

--Placeholder
SELECT p.id FROM posts p, comments_b c, users_b u, votes v
WHERE c.postid=p.id AND c.userid=p.owneruserid AND u.id=p.owneruserid
AND u.reputation > 100 AND v.postid = p.id AND v.userid = p.owneruserid
AND NOT EXISTS (SELECT 1 FROM postlinks l WHERE l.relatedpostid = p.id);

--Placeholder
SELECT p.id FROM posts p, comments c, users u, badges b
WHERE c.postid=p.id
AND u.id=p.owneruserid
AND u.upvotes+3 >= (SELECT COUNT(upvotes)
FROM users
WHERE u.creationdate = c.creationdate)
AND EXISTS (SELECT 1 FROM postlinks l WHERE l.relatedpostid > p.id)
AND u.id = b.userid
AND (b.name LIKE 'Autobiographer');

--Placeholder
SELECT p.id
FROM comments c, badges_tmp b, posts p, postlinks pl, users u,
(SELECT COUNT(upvotes)as nrupvotes FROM users) k
WHERE c.postid=p.id
AND u.upvotes + 3 >= k.nrupvotes
AND u.id=p.owneruserid
AND pl.relatedpostid > p.id
AND u.creationdate = c.creationdate
AND u.id = b.userid
AND (b.name LIKE 'Autobiographer');