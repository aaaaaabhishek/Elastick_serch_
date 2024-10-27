package com.Elastickserch_code_crud.elastick.config;

/**
 * Shard Management by the Master Node
 * Index Creation:
 *
 * When you create an index (using your createIndex method, for example), you specify the number of shards and replicas. This information is included in the index settings.
 * Shard Allocation:
 *
 * Once the index is created, the master node takes over the responsibility of allocating the shards to the appropriate nodes in the cluster:
 * Primary Shards: These are the original shards for your index, and the master node determines which data nodes will hold these shards.
 * Replica Shards: The master node also manages the allocation of replica shards. Replicas are copies of the primary shards and are distributed across different nodes to ensure redundancy and availability.
 * Dynamic Reallocation:
 *
 * If nodes are added or removed from the cluster, the master node dynamically reallocates shards as needed. For instance, if a data node goes down, the master node will automatically promote a replica shard to a primary shard and create new replicas on other available nodes.
 */

/**
 * Role of Master Node
 * The master node does not store any data itself (unless it’s also configured as a data node), but it orchestrates the cluster's state, including managing which nodes hold which shards and ensuring the cluster's health.
 * If you have multiple master-eligible nodes, one will be elected as the master node, and it will perform these shard management tasks.
 */
public class note {
}
