
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

**Group data on the state column by declaring a table that has state column as a partition key:**

|State|Name  |UserID|
|-----|------|------|
|TX   |John  |1     |
|CA   |Elaine|2     |
|CA   |Jay   |4     |
|CA   |Elen  |6     |
|NY   |Alex  |3     |
|NY   |Julio |5     |

Cassandra then groups the data base on declared partition key and distributes the data in cluster by hashing each partition key called **tokens**. Each Cassandra node has a predefined list of supported token intervals, and data is routed to the appropriate node based on the key value hash and this predefined token allocation in the cluster. 

### Data Replication and multiple DC support

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
SSTables (Sorted String Tables) are the immutable data files in Cassandra, storing data persistently on disk. These files are created by flushing memtables or streaming from other nodes. When you generate SSTables, Cassandra initiates the compaction processes to merge multiple SSTables into one. You should be able to see the new SSTable while the older SSTables become eligible for removal.

An SSTable comprises various distinct components stored in separate files, some of which include:
- **Data.db:** This file contains the actual data stored by Cassandra.
- **Index.db:** An index file that maps partition keys to specific positions within the Data.db file, aiding in efficient data retrieval.
- **Summary.db:** This file provides a sample subset (typically every 128th entry) of the information contained in the Index.db file, offering an overview for quicker data access.
- **Filter.db:** Cassandra employs a Bloom Filter in this file, which serves as a probabilistic data structure, assisting in determining if a partition key exists in the SSTable without requiring a disk seek.
CompressionInfo.db: This file holds metadata regarding the offsets and lengths of compressed chunks within the Data.db file, facilitating the decompression of stored data.

These distinct files within an **SSTable** collectively form an organized structure that enables efficient data storage, indexing, retrieval, and compression within the Cassandra database system.

