
# Apache Cassandra

## Introduction to Apache Cassandra

Apache Cassandra is **an open-source, distributed, decentralized, elastically scalable, highly available, fault tolerant, tunable, and consistent database** that bases its distribution design on Amazon's Dynamo and its data model on Google's Bigtable. 

Created at Facebook, it is now used at some of the most popular sites on the web. 

## Key features of Apache Cassandra

- Distributed and decentralized (Few NoSQL databases have this feature).
- Alway available with tunable consistency (between strong and eventual consistency). The conflict will be resolved during read operations.
- Fault tolerance (the other nodes in the cluster immediately recognize node' failures).
- High write throuhgput: At cluster level, writes can be distributed in parallel to all nodes holding replicas. By default, in Cassandra, there is no reading before writing. At node level, writes are done in node memory and later flushed on disk. All disk writes are sequential, append-like operations.
- Fast and linear scalability (by adding new nodes in the cluster) without the need to restart or reconfigure the services (elastically scalable).
- Multiple data center deployment support (geographically deployment).
- Cassandra query language (CQL), a friendly SQL-like query language (easy to interact with database).

- Does not support joins.
- Limited aggregation support.
- Limited support of transactions.
- No concept of referential integrity or foreign keys.

> [!NOTE]
> Decentralized means that all nodes perform the same functions.

### Data Distributed starts with a Query

**You have some initial data:**

|UserID|Name  |State|
|------|------|-----|
|1     |John  |TX   |
|2     |Elaine|CA   |
|3     |Alex  |NY   |
|4     |Jay   |CA   |
|5     |Julio |NY   |
|6     |Elen  |CA   |

**Query:** I would like to know about all users in one state.

**Group data on the state column by declaring a table that has 'state' column as a partition key:**

|State|Name  |UserID|
|-----|------|------|
|TX   |John  |1     |
|CA   |Elaine|2     |
|CA   |Jay   |4     |
|CA   |Elen  |6     |
|NY   |Alex  |3     |
|NY   |Julio |5     |

Cassandra then groups the data base on declared partition key and distributes the data in cluster by hashing each partition key called **tokens**. Each Cassandra node has a predefined list of supported token intervals, and data is routed to the appropriate node based on the key value hash and this predefined token allocation in the cluster. 

> [!NOTE]
> Each partition is distributed to one of the cluster nodes.

### Data Replication and multiple DC support

## Apache Cassandra Data Model

### Logical Entities

- Table: the logical entity that organizes data storage at cluster and node level according to a declared schema.
- Keyspace: the logical entity that contains one or more tables. Replication and data center's distribution are defined at the keyspace level.
- Data is organized in tables containing rows of columns.
- Table can be created, dropped, and altered at runtime without blocking updates and queries.

```sql
CREATE TABLE basic_cassandra.groups(
    groupid int,
    groupname text STATIC,
    username text,
    age int,
    PRIMARY KEY((groupid), username)
)   
```

**Primary key:**
- Cannot be changed once defined.
- Has two main roles:
  - Optimizes the read performance for queries on the tables.
  - Provide uniqueness to the entities.
- Has two components:
  - Partition key (mandatory). Data is grouped into partitions and distributed on the cluster nodes based on partition key.
  - Clustering key (optional). 
    - Clustering key specifies the order that the data is arranged in inside the partition for the fast retrieval or similar values. 
    - Clustering key can contain single or multiple columns.

**Table types:**
- Static table: has a primary key that contains only the partition key. In static tables, partitions have only one entry. 
- Dynamic table: has primary key that contains both partition key and clustering key.

> [!IMPORTANT]
> Data modeling is the process to build a primary key that optimizes query execution time.

> [!NOTE]
> Choose a partition key that:
> - Starts answering your query and spread the data uniformly in the cluster.
> - Reduces the amount of data to be read from a partition.
> To further optimize the query model, order clustering keys according to the query.


## Cassandra Query Language

> [!IMPORTANT]
> CQL does not support JOIN statement. Names for identifiers created using uppercase are always stored in lowercase.


## Use Cases

**Scenarios:**
- When the application is write intensive and the number of writes exceed the number of read requests.
- When using append-like type of data (not many updates and deletes).
- When you can predefine your queries and your data access is by a known primary key.
- When there is no need for joins and aggregations.

**Best fit for:**
- Globally alway available online services, such as Netflix, Sportify and Uber.
- eCommerce websites (store transactions, interactions for prediction of customer behavior, users' profiles, and shopping history).
- Time series applications (Monitoring servers access logs, weather updates from sensors, tracking packages).

## Architecture of Cassandra

### Topology

Cassandra is a distributed system architecture. The basic component of Cassandra's architecture is the standalone unit, node. Nodes can be added or removed without affecting the system's availability. Every node can run all database operations and handle client requests independently, eliminating the need for a primary node and communicate with other nodes through a peer-to-peer protocol.

The gossip protocol enables nodes to exchange details and information, updating each node about the status of all other nodes. A node performs gossip communications with up to three other nodes every second. The gossip messages follow a specific format and use version numbers to communicate efficiently. So, each node can build the entire metadata of the cluster (which nodes are up/down, what the tokens allocated to each node are, and so on).

### Components

#### Memtable

Memtables serve as in-memory structures within Cassandra, buffering write operations before being written onto disk. Typically, each table has an active Memtable. Eventually, these Memtables are flushed to disk, transforming into immutable SSTables (Sorted String Tables). The triggering of Memtable flushes can occur through various methods:
- Exceeding a predefined threshold for Memtable memory usage.
- Approaching the maximum size of the CommitLog, which prompts Memtable flushes to free up CommitLog segments.
- Setting specific time intervals to trigger flushes on a per-table basis.

#### Commit log
Commit logs in Cassandra function as append-only logs, capturing all local mutations on a specific Cassandra node. Before you write data to a Memtable, you must record it in a commit log. This process ensures durability in the event of an unexpected shutdown. Upon restarting, any mutations in the commit log are applied to the Memtables, guaranteeing data consistency and recovery in the Cassandra database system.

#### SSTables
SSTables (Sorted String Tables) are the immutable data files in Cassandra, storing data persistently on disk. These files are created by flushing memtables or streaming from other nodes. When you generate SSTables, Cassandra initiates the **compaction** processes to merge multiple SSTables into one. You should be able to see the new SSTable while the older SSTables become eligible for removal.

An SSTable comprises various distinct components stored in separate files, some of which include:
- **Data.db:** This file contains the actual data stored by Cassandra.
- **Index.db:** An index file that maps partition keys to specific positions within the Data.db file, aiding in efficient data retrieval.
- **Summary.db:** This file provides a sample subset (typically every 128th entry) of the information contained in the Index.db file, offering an overview for quicker data access.
- **Filter.db:** Cassandra employs a Bloom Filter in this file, which serves as a probabilistic data structure, assisting in determining if a partition key exists in the SSTable without requiring a disk seek.
- **CompressionInfo.db:** This file holds metadata regarding the offsets and lengths of compressed chunks within the Data.db file, facilitating the decompression of stored data.

These distinct files within an **SSTable** collectively form an organized structure that enables efficient data storage, indexing, retrieval, and compression within the Cassandra database system.

### Write process at node level

Writes are distributed across nodes in the cluster, utilizing a coordinator node that manages the operation and replicates data to appropriate replicas based on the configured consistency level.

- Logging data in the commit log.
- Writing data to the Memtable.
- Flushing data from the Memtable.
- Storing data on disk in SSTables.

### Read at node level

Since you can fragment data on disk into several SSTables, the reading process needs to identify which SSTables most likely contain info about the querying partitions. The Bloom Filter information makes this selection through the following steps:

1. Checks the Memtable.
2. Checks Bloom filter.
3. Checks partition key cache, if enabled.
4. If the partition is not in the cache, the partition summary is checked.
5. Then, the partition index is accessed.
6. Locates the data on the disk.
7. Fetches the data from the SSTable on disk.
8. Data is consolidated from Memtable and SSTables before being sent to the coordinator.

![Read at node level](https://cf-courses-data.s3.us.cloud-object-storage.appdomain.cloud/IBMSkillsNetwork-CS0101EN-Coursera/images/write.png)


### Read/Select Rules

- Start the query using Partition Key to limit reads to specific nodes that contain the data.
- Follow the order of the Primary Columns for best performance.
- Can perform filtering on Partition Key. Partition Key only supports Equal and In operations.

> [!NOTE]
> TOMBSTONES is a special value to indicate data has been deleted and time of the delete. 


## Glossary

| Term             | Definition                                                                                                                                                                                                                    |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| BSON             | Binary JSON, or BSON, is a binary-encoded serialization format used for its efficient data storage and retrieval. BSON is similar to JSON but designed for compactness and speed.                                             |
| Aggregation      | Aggregation is the process of summarizing and computing data values.                                                                                                                                                           |
| Availability     | In the context of CAP, availability means that the distributed system remains operational and responsive, even in the presence of failures or network partitions. Availability is a fundamental aspect of distributed systems. |
| CAP              | CAP is a theorem that highlights the trade-offs in distributed systems, including NoSQL databases. CAP theorem states that in the event of a network partition (P), a distributed system can choose to prioritize either consistency (C) or availability (A). Achieving both consistency and availability simultaneously during network partitions is challenging. |
| Cluster          | A group of interconnected servers or nodes that work together to store and manage data in a NoSQL database, providing high availability and fault tolerance.                                                                  |
| Clustering key   | A clustering key is a primary key component that determines the order of data within a partition.                                                                                                                             |
| Consistency      | In the context of CAP, consistency refers to the guarantee that all nodes in a distributed system have the same data at the same time.                                                                                        |
| CQL              | Cassandra Query Language, known as CQL, is a SQL-like language used for querying and managing data in Cassandra.                                                                                                              |
| CQL shell        | The CQL shell is a command-line interface for interacting with Cassandra databases using the CQL language.                                                                                                                    |
| Decentralized    | Decentralized means there is no single point of control or failure. Data is distributed across multiple nodes or servers in a decentralized manner.                                                                            |
| Dynamic table    | A dynamic table allows flexibility in the columns that the database can hold.                                                                                                                                                |
| Joins            | Combining data from two or more database tables based on a related column between them.                                                                                                                                       |
| Keyspace         | A keyspace in Cassandra is the highest-level organizational unit for data, similar to a database in traditional relational databases.                                                                                          |
| Partition Key    | The partition key is a component of the primary key and determines how data is distributed across nodes in a cluster.                                                                                                         |
| Partitions       | Partitions in Cassandra are the fundamental unit of data storage. Data is distributed across nodes and organized into partitions based on the partition key.                                                                   |
| Peer-to-peer     | The term peer-to-peer refers to the overall Cassandra architecture. In Cassandra, each node in the cluster has equal status and communicates directly with other nodes without relying on a central coordinator. If a primary node fails, another node automatically becomes the primary node. |
| Primary key      | The primary key consists of one or more columns that uniquely identify rows in a table. The primary key includes a partition key and, optionally, clustering columns.                                                         |
| Replication      | Replication involves creating and maintaining copies of data on multiple nodes to ensure data availability, reduce data loss, fault tolerance (improve system resilience), and provide read scalability.                      |
| Scalability      | Scalability is the ability to add more nodes to the cluster to handle increased data and traffic.                                                                                                                              |
| Static table     | A static table has a fixed set of columns for each row.                                                                                                                                                                       |
| Table            | A table is a collection of related data organized into rows and columns.                                                                                                                                                      |
| Transactions     | Transactions are sequences of database operations (such as reading and writing data) that are treated as a single, indivisible unit.                                                                                          |
