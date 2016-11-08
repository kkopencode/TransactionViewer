# Transaction Viewer - Android

## Summary
The app is aim at helping the executives to monitor every product that the company trades with and the and the sales of those products. This app is built for Android code test. For demo purpose, the data are stored in json file instead of fetching from server.

## Structure
This applicaton consist of two screens and will load the transactons data and currency exchange rate in assets folder. 

The Main screen will list all the products and show the count of the transactions of that product. Click on individual product will show the transaction details.

The Transaction screen will list out all the transasction related to that product. Besides the original trading currency, each transaction will show the amount in GBP as well. A sum of the transaction amount in GBP will display on the top of the screen.

## Currency Conversion
Here is the logic for currency rate conversion

1. If the source and target currency pair can be found in the supplied rates.json, return that rate

2. If the source and target pair not found in supplied pair. 

  * A directed graph (see assumption 1) will be created from the supplied rates. Currency is the Node and connected by "From", "To" relation in rates.json. 
  * Find the path from source to target currency.
    * It will find the Shortest Path (see assumption 2) in the graph of the source and target currency in case of multiple path of conversion is available. Breadth-First Search was used to find the shortest path.
  * Convert the currency from source to target currency along the path return from BFS.

For example:  
Given  
```
          /-->- GBP---->----CAD  
         /        \           \  
 USD ->--\         \           \  
          \--->---- AUD --->-- EUR  
```       
If required to convert from USD to EUR, it will convert through USD -> AUD -> EUR.

##### Assumption:

1. Assume that the given "From", "To" is one way only, if it can be converted reversely, it would state in the rates.json.  
  E.g. If USD can convert to GBP and vice versa, it will be given in rates.json  
  ```
  {"from":"USD","rate":"0.77","to":"GBP"},{"from":"GBP","rate":"1.3","to":"USD"}
  ```

2. Assume that the rate should be better for least amount of conversion normally. ( There are transaction fee for conversion in real world )
