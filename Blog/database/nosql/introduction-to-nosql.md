

# Introduction to NoSQL Database

> [!QUOTE]
> It is a capital mistake to theorize before one has data.
> <div>- Sherlock Holmes -</div>

## NoSQL Database Overview

NoSQL stands for 'Not Only SQL', also known as Non-relational Databases. Introduced during an open-source event on distributed databases, NoSQL refers to a group of databases with a non-relational architecture. These databases share the following characteristics:
- NoSQL databases offer new methods for storing and querying data.
- They address many of the challenges faced by modern applications.
- Unlike relational databases, NoSQL does not require a fixed schema, making it easier for applications to scale.
- NoSQL is a distributed system that provides fault tolerance and high availability.

## History of NoSQL Database

- From 1970 to 2000 was the dominant period of RDBMS (Oracle, MSSQL Server, MySQL...).
- In the period from 2000 to 2005, large-scale applications serving millions of users faced performance and availability issues. Large technology corporations like IBM, and Google developed new technologies to build these large-scale applications.
- From 2005 to 2010, many open-source databases were born such as MongoDB, Apache Cassandra, Redis... In 2010, cloud-based database technology DBaaS (Database as a Service) was born.

## NoSQL Database Classification

NoSQL Database is divided into 4 types:
- Key-Value Database
- Document-store Database
- Column-based Database
- Graph Database

Most NoSQL Databases are built to scale horizontally and share data more easily than RDBMS. To achieve this, they use a single global unique key to simplify data partitioning. Additionally, they serve specific purposes better than RDBMS. They are suitable for agile software development methods more than RDBMS.

## Benefits of NoSQL Database

- Provides a flexible data model, making it easier to store unstructured data.
- Provides horizontal and vertical scaling capabilities.
- High performance and fast response times for large concurrent requests and large data volumes.
- Works in distributed environments providing fault tolerance and high availability.
- Reduces costs by being deployable on the cloud.
- Has more diverse data structures than relational databases with only column and row structures.
- Has specific capabilities such as indexing and querying (geospatial search), data replication robustness, and modern HTTP API.

> [!NOTE]
> Large applications often use a combination of several types of NoSQL.

## Characteristics and Applications of each type of NoSQL Database

### Key-value Databases

#### Architecture
The simplest type of NoSQL Database. Key-value Databases store data in a hashmap of key-value pairs. They have the following characteristics:
- Efficient read and write operations due to optimization for fast access based on keys.
- Easy to scale due to simple structure and data distribution across nodes.
- Use caching for fast access.
- Provide session management.
- Work with distributed systems.

#### Suitable use cases:
- High-performance applications performing simple CRUD operations on data with low connectivity like storing and retrieving data from sessions.
- Applications that need to store user information and preferences.
- E-commerce applications storing cart data.

#### Unsuitable use cases:
- Not suitable for complex queries requiring joining many data pieces and many-to-many relationships.
- Only suitable for operations with unique keys.
- Values are not clear, so they are not efficient for querying and indexing.

#### Some typical databases in this group are:
- Redis
- Memcached
- Amazon DynamoDB

### Document-store Databases

#### Architecture
Document-store Databases store data as a document (usually JSON, BSON, or XML format) to make values visible for queries. They have the following characteristics:
- Offer a flexible schema.
- Content of document databases can be indexed and queried.
- Efficient CRUD operations.
- Provide horizontal scalability by partitioning data into clusters.
- Only guarantee atomic opetions on single document.

#### Suitable use cases:
- Event logging for apps and processes: each event instance is presented by a document.
- Online blogs: each user, post, comment, and action is presented by a document.
- Operational databases and metadata for web apps and mobile apps.

#### Unsuitable use cases:
- Require ACID transactions.
- The data is an aggregate-oriented design.

#### Some typical databases in this group are:
- MongoDB
- Couchbase
- Apache CouchDB
- Amazon DocumentDB


**A blog example (MongoDB):**

```json
{
    "_id":1,
    "title": "Sample Blog Post",
    "content": "This is the content of the blog post...",
    "author": {
        "name": "John Doe",
        "email": "john@example.com",
        "bio": "A passionate blogger.",
        "created_at": "2023-09-20T00:00:00Z"
    },
    "created_at": "2023-09-20T08:00:00Z",
    "tags": ["mongodb","blogging","example"],
    "comments": [
        {
            "text": "Great post!",
            "author": "Emily Johnson",
            "created_at": "2023-09-20T10:00:00Z"
        },
        {
            "text": "Thanks for sharing!",
            "author": "James Martin",
            "created_at": "2023-09-20T11:00:00Z"
        }
    ]
}
```


**Create text index**

```js
db.articles.createIndex({ subject: "text" });
```

**Search**

```
db.posts.find({ $text: { $search: "digital life" }});
```


### Column-based Databases (aka. Wide-column Databases)

#### Architecture
Column-based Databases organize data into columns or groups of columns instead of rows. They have the following characteristics:
- Allow efficient access to specific columns.
- Distributed architecture provides high availability and scalability.

#### Suitable use cases:
- IoT applications managing large amounts of sensor data efficiently thanks to processing data marked with time on a large scale, called time series data analysis.
- Storage and analysis of user preferences and behaviors providing personalization.
- Large-scale data analysis.
- Data warehousing.
- Handle deployment across clusters of nodes.
- Used for event handling and blogs.
- Counters.
- Data with an expiration value.

#### Unsuitable use cases:
- ACID transactions.

#### Some typical databases in this group are:
- Apache Cassandra
- Apache HBase
- Hypertable

**An E-commerce example:**

OrderID: [1001,1002]
CustomerID: [C001,C002]
ProductID: [P101,P102]
OrderDate: [01-09-2024,02-09-2024]
Quantity: [2,1]
TotalPrice: [50,30]

**Query: display the total price for all orders of product with ID P101**

The query only needs to read the ProductID and TotalPrice column data.

### Graph Databases

#### Architecture
Graph Databases organize data in graph format, designed to manage highly connected data. They have the following characteristics:
- Analyze data using a graph data model, allowing efficient traversal and querying of complex relationship data.
- Optimized for queries related to relationships, ideal for social network applications, recommendation systems, and network analysis.
- They are ACID transaction compliant.

#### Suitable use cases:
- Social network applications.
- Routing, spacial, and map apps
- Recommendation systems that need to analyze

#### Unsuitable use cases:
- Applications that need to scale horizontally.
- When trying to update all or a subset of nodes with a given parameters.

#### Some typical databases in this group are:
- Neo4j
- OrientDB
- Amazon Neptune
- ArangoDB Memcached

## NoSQL Database Deployment Options

### On-premises Deployment

On-premises deployment involves hosting the entire database infrastructure within the organization's physical location or a dedicated data center.

![On-premises Deployment](https://cf-courses-data.s3.us.cloud-object-storage.appdomain.cloud/IBMSkillsNetwork-CS0101EN-Coursera/images/200543.012-01.png)

**Advantages:**
- Full control: Organizations have complete control over hardware specifications, software configurations, and security measures.
- Compliance: On-premises solutions might be necessary for industries with stringent regulatory or compliance requirements.

**Challenges:**
- Upfront costs: High initial investments in hardware, infrastructure, and skilled personnel.
- Scalability: Limited scalability compared to cloud alternatives, which might result in challenges during periods of rapid growth.

### Cloud Deployment

Cloud deployment involves utilizing cloud service providers to host and manage databases over the internet.

**Advantages:**
- Scalability: Resources can be scaled up or down based on demand, providing flexibility and cost savings.
- Cost-effectiveness: Pay-as-you-go pricing allows organizations to pay only for the resources they consume.
- Automation: Cloud providers handle routine maintenance tasks, updates, and backups.

**Challenges:**
- Connectivity dependencies: Relies on internet connectivity, which might pose issues in areas with limited or unreliable access.
- Security concerns: Requires robust security measures to protect sensitive data from unauthorized access.

### Hybrid Deployment

Hybrid deployment combines on-premises and cloud solutions, allowing organizations to distribute data and workloads based on specific needs.


![Hybrid Deployment](https://cf-courses-data.s3.us.cloud-object-storage.appdomain.cloud/IBMSkillsNetwork-CS0101EN-Coursera/images/200543.012-02.png)

**Advantages:**
- Flexibility: Companies can keep sensitive data on-premises while using the cloud for scalable and dynamic workloads.
- Disaster recovery: Companies can use cloud storage for backups, enhancing disaster recovery capabilities.

**Challenges:**
- Integration complexity: Integration complexities require effective integration between on-premises and cloud environments for seamless operation.
- Management overhead: Monitoring and managing two different environments might increase administrative complexity.

### Database as a Service (DBaaS)

DBaaS (Interface/API) provides a fully managed database solution where the service provider handles administrative tasks, allowing organizations to focus on application development.

![DBaaS](https://cf-courses-data.s3.us.cloud-object-storage.appdomain.cloud/IBMSkillsNetwork-CS0101EN-Coursera/images/200543.012-04.png)

**Advantages:**
- Reduced overhead: Organizations benefit from reduced administrative tasks, including maintenance, backups, and updates.
- Rapid deployment: Quick deployment without the need for in-depth database expertise.

**Challenges:**
- Limited control: Organizations have less control over underlying infrastructure, which might be a concern for some organizations.
- Data security: Trusting a third-party provider with sensitive data raises security and privacy considerations.

### Containerized Deployment

Containerization involves packaging the database and its dependencies into containers for consistent deployment across various environments.

![Containerized Deployment](https://cf-courses-data.s3.us.cloud-object-storage.appdomain.cloud/IBMSkillsNetwork-CS0101EN-Coursera/images/200543.012-05.png)

**Advantages:**
- Portability: Containers ensure consistent operation across development, testing, and production environments.
- Resource efficiency: Lightweight containers consume fewer resources compared to traditional virtual machines.

**Challenges:**
- Orchestration complexity: Production-scale deployment requires knowledge of container orchestration tools like Kubernetes.
- Learning curve: Teams might need to familiarize themselves with containerization concepts and tools.

### Serverless Deployment

Serverless architecture provisions and scales database resources automatically based on demand, with organizations paying for actual usage.

![Serverless Deployment](https://cf-courses-data.s3.us.cloud-object-storage.appdomain.cloud/IBMSkillsNetwork-CS0101EN-Coursera/images/200543.012-06.png)

**Advantages:**
- No manual provisioning: No need for manual provisioning or maintenance of server resources.
- Cost savings: Efficient resource utilization leads to cost savings as organizations only pay for the resources consumed.

**Challenges:**
- Limited control: Organizations have less control over underlying infrastructure, which might concern some organizations.
- Applicability: Serverless might not be suitable for all types of databases or workloads due to architectural constraints.
