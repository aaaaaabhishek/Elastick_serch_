1. Term Query
Purpose: Matches documents that contain an exact value in a specific field.
Usage: Use this when you want to find documents that contain a single exact match for a field.

TermQuery termQuery = TermQuery.of(t -> t
        .field("user_id")
        .value(FieldValue.of("12345")) // Matches documents where user_id is exactly "12345"
);

2. Terms Query
Purpose: Matches documents that contain any of the values in a specified list for a particular field.
Usage: Use this when you want to find documents that may contain multiple values.

List<FieldValue> fieldValues = List.of(FieldValue.of("12345"), FieldValue.of("67890"));

TermsQuery termsQuery = TermsQuery.of(t -> t
        .field("user_id")
        .terms(ts -> ts
                .value(fieldValues) // Matches documents where user_id is either "12345" or "67890"
        )
);

3. Match Query
Use Case: Used for full-text search. It analyzes the input and matches documents based on the tokens generated from the input.
Example: If you're searching for documents containing the word "Elasticsearch" in the description field, you would use a MatchQuery.

MatchQuery matchQuery = MatchQuery.of(m -> m
        .field("description")
        .query("Elasticsearch")
);

4. Bool Query
Use Case: Combines multiple queries using logical operators (must, should, must_not). This is useful for creating complex queries.
Example: If you want to find documents where user_id is 12345 and the status is active, you can use a BoolQuery.

BoolQuery boolQuery = BoolQuery.of(b -> b
        .must(m -> m
                .term(t -> t
                        .field("user_id")
                        .value(FieldValue.of("12345"))
                )
        )
        .filter(f -> f
                .term(t -> t
                        .field("status")
                        .value(FieldValue.of("active"))
                )
        )
);

5. Range Query
Use Case: Finds documents with field values within a specified range. Commonly used for dates or numerical values.
Example: If you want to find documents where the date is between 2023-01-01 and 2023-12-31, you would use a RangeQuery.
RangeQuery rangeQuery = RangeQuery.of(r -> r
        .field("date")
        .gte(JsonData.of("2023-01-01"))
        .lte(JsonData.of("2023-12-31"))
);
8. Wildcard Query
Use Case: Used for pattern matching. Useful for cases where you need to find values that match a specific pattern.
Example: If you're searching for documents where the name starts with "John", you would use a
 WildcardQuery.WildcardQuery wildcardQuery = WildcardQuery.of(w -> w
        .field("name")
        .value("John*")

);

6.Exists Query:

Purpose: Checks if a field has a non-null, non-empty value in the document.

ExistsQuery existsQuery = ExistsQuery.of(e -> e
    .field("fieldName")
);
Example Use Case: Find all documents where the description field is populated with a non-null value.
Common Scenario: Often used to filter out documents missing specific data.

7.Term Query:

Purpose: Checks if a field contains a specific value, including an empty string ("") if needed.


TermQuery termQuery = TermQuery.of(t -> t
    .field("fieldName")
    .value(FieldValue.of("specificValue"))
);

Example Use Case: Retrieve documents where the description field is exactly an empty string ("").
Common Scenario: Used to filter documents by exact matches on specific fields.

