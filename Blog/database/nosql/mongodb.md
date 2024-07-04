
# MongoDB

## Introduction to MongoDB

MongoDB is a document-store NoSQL database. MongoDB documents of similar type are grouped into a collection. MongoDB also allows storing subdocuments to group secondary information together and supporting lists of values.

```json
{
    "name": "John",
    "studentId": 20120432,
    "enrolled": true,
    "address": {
        "city": "Ho Chi Minh",
        "district": "Thu Duc"
    },
    "interests": ["football", "badminton"]
}
```

> [!NOTE]
> Collections are analogous to tables in a relational database.

> [!NOTE]
> MongoDB helps you model data as you read and write. It gives you the power to bring in any structured or unstructured data, and provides high availability.

> [!NOTE]
> MongoDB can ingest several types of data from various sources and consolidate the data into one view of all data.

## Advantages of MongoDB

- MongoDb allows flexibility with schema

```json
{
    "city": "London",
    "postcode": "W1 1SU"
}
```

```json
{
    "city": "New York",
    "zip": "10002"
}
```

- Code-first approach: There is no complex table definitions, so you can start writing your first data as soon as you connect to MongoDB.
- Evolving schema.
- Querying using **Mongo Query Language (MQL)** and complex analytics using **aggregation pipelines**.
- High availability.

> [!NOTE]
> MongoDB Query Language is a query language specific to MongoDB used to retrieve and manipulate data in the database.

> [!NOTE]
> The aggregation pipeline in MongoDB allows for data transformation and processing using a series of stages, including filtering, grouping, sorting, and projecting.

## MongoDB SHell

**Connect to mongodb server:**
```shell
mongosh "URI"

mongosh --host mongodb0.example.com --port 28015

mongosh "mongodb://mongodb0.example.com:28015" --username alice --authenticationDatabase admin

mongosh -u root -p PASSWORD --authenticationDatabase admin local
```

**List databases:**
```shell
show dbs
```

**Switch database:**
```shell
use <database_name>
```

**Create a collection:**
```shell
db.createCollection("<collection_name>")
```

**List collections:**
```shell
show collections
```

**Count the number of documents:**
```shell
db.landmarks.countDocuments()
```

**Count the number of documents that match criteria:**
```shell
db.landmarks.count({"country":"VN"})
```

**Get the documents:**
```shell
db.landmarks.find()

db.landmarks.find({"country":"VN"})

db.landmarks.findOne()
db.landmarks.findOne({"country":"VN"})

db.<collection_name>.find({ field: { $lt: value, $gt: value } })

db.<collection_name>.find({ $or: [ { field1: value1 }, { field2: value2 } ] })
```

**Get first 3 documents in the collection:**
```shell
db.landmarks.find().limit(3)
```

**Project specific fields (1 is true and 0 is false):**
```shell
db.landmarks.find({},{"country":1})
```

**Insert documents to collections:**
```shell
db.landmarks.insert({"name":"Statue of Liberty","city":"New York","country":"USA"})

landmark_list = [
    {"name":"Statue of Liberty","city":"New York","country":"USA"},
    {"name":"Ho Chi Minh city museum","city":"HCM","country":"VN"}
]

db.landmarks.insertMany(landmark_list)
```

**Replace a document:**
```shell
db.landmarks.replaceOne({"country":"VN"},updated_landmark)
```

**Update documents:**
```shell
changes = {"$set":{"country":"VietNam"}}

db.landmarks.updateOne({"country":"VN"},changes)

// the first arg is filter, the second is update .
// adding a field rating to all documents.
db.landmarks.updateMany({},{$set:{"rating":1}})

// increment rating for Statue of Liberty by 1.
db.landmarks.updateOne({"name":"Statue of Liberty"},{$inc:{"rating":1}})
```

**Delete documents:**
```shell
db.landmarks.deleteOne({"country":"VN"})

db.landmarks.deleteMany({"country":"VN"})
```

**Disconnect from the server:**
```shell
exit
```

## Indexes

MongoDB indexes are special data structures which store the field you are indexing. They also store the location where the document is stored on disk.

If you are sorting on a field which is already indexed, MongoDB doesn't need to sort it again because it stores indexes in a balanced tree.

**Create index according to ascending order:**
```shell
db.courseEnrollment.createIndex({"courseId":1})

// make the searching and sorting more efficient
db.courseEnrollment.createIndex({"courseId":1,"studentId":1})

```

## Aggregation

The Aggregation Framework is a series of operations that you apply to your data to get a sought-after outcome.

```shell
db.courseResults.aggregate([
    { 
        $match: { "year": 2020 } 
    },
    { 
        $group: 
        {
            "_id": "$courseId", "avgScore": { $avg: "$score" } 
        } 
    }
]) 
```

```shell
db.students.aggregate([
    {
        $project: { firstName: 1, lastName: 1 },
    },
    {
        $sort: { lastName: -1 }
    }
])
```

```shell
db.students.aggregate([
    {
        $count: "totalStudents"
    }
])
```

**Aggregation use cases:**
- Reporting: track student progress.
- Analysis: determine the average product sales by country.

## Replication and Sharding

### Replication

A MongoDB Replica Set is a cluster of data-bearing nodes, each maintaining a copy of the same data.

Replication creates redundancy, but it provides high available databases.

> [!NOTE]
> The primary node is the active, writable node that processes all write operations. Secondary nodes replicate data from the primary and can be used for read-operations.

> [!CAUTION]
> What happens on primary node is replicated on the secondary nodes, so replication cannot save you from disasters, such as accidentally deleting your database.

**The replication process:**
1. The application writes changes to the primary node.
2. The primary node records those changes including timestamps in its operations log known as the Oplog.
3. The secondary nodes observe primary node's Oplog for new changes, they copy and apply the new changes by first recording those changes in their Oplog with the timestamp that indicate when they receive the change and reference the primary Oplog timestamps to record only the most recent changes. 

> [!NOTE]
> Replication lag refers to the delay in data replication from a primary node to its secondary nodes in a replica set. Replication lag can impact the consistency of secondary data.

### Sharding

When data is growing beyond hardware capacity by:
- Investing to bigger and faster hardware to increase capacity (vertical scale).
- Partitioning the biggest collections (horizontal scale).

Sharding refers to the practice of partitioning a database into smaller, more manageable pieces called shards to distribute data across multiple servers.

Sharding helps with increased throughput and capacity. It help adhere to legal requirements for data storage.

### Election

> [!NOTE]
> The primary node is the only node that can accept operations including insert, update, delete from client applications.

An election is process of selecting a new primary node.

MongoDB can automatically elect one of the secondary nodes as the new primary node. The election of a new primary node happens when:
- The current primary node becomes unavailable.
- A new replica set is initialized and needs to choose its initial primary.
- A database admin initiates a manual failover for maintenance or upgrades.

