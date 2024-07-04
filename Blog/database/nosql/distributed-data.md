

# Working with Distributed Data

## ACID vs. BASE Operations

Relational databases use the **ACID** model, whereas NoSQL databases generally use the **BASE** model.

### ACID

> [!NOTE] 
> The ACID acronym stands for **A**tomicity, **C**onsistency, **I**solation, **D**urability.

**Atomicity** means that an operation is indivisible and either completed fully or is completely rolled back. It ensures that the database remains in a consistent state.

**Consistency** means that constraints are enforced for every committed transaction. That implies that all keys, data types, checks and triggers are successful and no constraint violation is triggered.

**Isolation** refers to the property that multiple transactions can run concurrently without affecting each other.

**Durability** guarantees that once a transaction is committed, its changes are permanent and will survive any system failures.

The ACID models are models good fit for businesses that deal with online transaction processing, such as financial institutions or data warehousing application.

### BASE
> [!NOTE]  
> The BASE acronym stands for **B**asically **A**vailable, **S**oft State, **E**ventually Consitent.

**Availability** means that the distributed system remains operational and responsive, even in the presence of failures or network partitions. Availablity is a fundamental aspect of distributed systems.

A **soft state** acknowledges that the system's state might be transiently inconsistent due to factors like network partitions or concurrent updates. And it's willing to accept a certain level of inconsistency or uncertainty in the data temporarily.

An **eventually consistent** system reaches a consistent state, where all nodes have the same data given that there are no new updates.

NoSQL has few requirements for immediate consistency, data freshness, and accuracy. This tradeoff brings other benefits, such as availability, scale, and resilience. They are usually used by marketing and customer service companies, social media apps, and worldwide available online services, such as Netflix, Spotify, and Uber.

The BASE models allows for greater system availability and scalability, but it doesn't offer a guaranteed consistency of replicated of data at right times.

## Distributed Databases

A distributed database is a collection of multiple interconnected databases, which are spread physically across various locations that communicate via a computer network. It's physically distributed across by fragmenting (sharding) and replicating data. 

> [!NOTE] 
> The distributed database follows the BASE consistency model.

### Fragmentation

To store on all servers of a distributed system, you need to break your data into smaller pieces. This process is known as fragmentation/partitioning/sharding of data. This process is usually done by the key value record in two ways:
- Grouping keys lexically.
- Grouping records that have the same key and placing them on the same server.

### Replication

Replication is a process that protect data for node failures. All data fragments are store redundantly in two or more sites. It increases the availability of data in different sites. If one node fails, the piece of data can be retrieved from another node. However, it has certain disadvantages as well. Data need to be consistently synchronized. Any change made at one site needs to be replicated to every site where that related data is stored.

### Advantages of Distributed Systems

- Allow more reliability and availability.
- Improved performance, especially for high volumns of data.
- Query processing time is reduced which also helps improve performance.
- Can easily grow or scale to increase your system capacity just by add by new server to clusters. 
- Provide continuous operations with no more reliance on the central site.

### Chanllenges of Distributed Systems

Concurrency control: The same piece of data is stored in multiple locations, if you change your data, you need to ensure data synchronization is secured.  

## CAP Theorem

There are three essential system requirements necessary for the successful design, implementation, and deployment of applications in distributed systems. These are **C**onsistency, **A**vailability, and **P**artition Tolerance.

CAP theorem states that in the event of a network partition, a distributed system can choose to prioritize either consistency or availability. Achieving both consistency and availability simultaneously during network partitions is challenging.

> [!NOTE]
> Consistency refers to the guarantee that all nodes in a distributed system have the same data at the same time.

> [!NOTE]
> A partition is lost or temporarily delayed connection between nodes.

> [!NOTE]
> Partition tolerance is the ability of a distributed system to continue functioning even when network partitions or communication failures occur.

> [!NOTE]
> MongoDB chooses consistency as the primary design driver of the solution.

> [!NOTE]
> The CAP theorem is also called Brewer's theorem because it was first advanced by Professor Eric A. Brewer.

## Vector Databases

A vector database is designed to store, manage, and index massive quantities of high-dimensional vector data efficiently.

### Benefits of Vector Databases

Unlike other database types that require an exact term search, you can use a vector database to conduct similarity searches and retrieve data according to their vector distance or likeness.

For example, you can use a vector database to perform the following tasks:
- Recommend TV shows to watch based on your current viewing habits.
- Locate related products based on the first product's features and ratings when shopping online.

Because related vector data exists mathematically closer to each other within the database, search and data delivery times are faster. So rather than having to perform additional analysis techniques to retrieve related data, the trained model and vector database delivers relevant search results faster.

### Popular Vector Databases

- Chroma
- Pinecone
- Weaviate
