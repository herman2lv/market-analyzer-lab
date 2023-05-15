# Tech Market Data Analyzer
*Author: Herman Rabinkin*

Project intended to collect data from catalog.onliner.by and analyze historical data 
about prices of different categories of tech products

User could define both period and categories of products to be analyzed

To run project environment should be configured with 
`spring.profiles.active` to be set `"app"` and any of `"test"`/`"dev"`/`"prod"`
For example, set environment variable `spring.profiles.active=app,dev`

For profiles `"dev"`/`"prod"` database must be available.

For `"prod"` profile DB connection credentials should be externalized to environment variables

Expected database connection configuration is at [`application.yaml`](/src/main/resources/application.yaml)
