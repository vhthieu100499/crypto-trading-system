# crypto-trading-system
Develop a crypto trading system with SpringBoot framework and in memory H2 Database.

1. Price aggregation from the source below:
Binance
Url : https://api.binance.com/api/v3/ticker/bookTicker
Houbi
Url : https://api.huobi.pro/market/tickers
Create a 10 seconds interval scheduler to retrieve the pricing from the source
above and store the best pricing into the database.
Hints: Bid Price use for SELL order, Ask Price use for BUY order
-----> FUNCTION:
	batchReceiveBestTrading()

2. Create an api to retrieve the latest best aggregated price.
-----> API:
	localhost:8080/api/tradingTicket/getLatestBestAggregatedPriceTicket

3. Create an api which allows users to trade based on the latest best aggregated
price.
Remarks: Do not integrate with other third party system
-----> API: 
	localhost:8080/api/tradingTicket/buy?userId=1&tradingTicketId=1
	localhost:8080/api/tradingTicket/sell?userId=1&tradingTicketId=1

4. Create an api to retrieve the user’s crypto currencies wallet balance
-----> API:
	localhost:8080/api/user/getBalance?userId=1

5. Create an api to retrieve the user trading history.
-----> API:
	localhost:8080/api/tradingTicket/getTradingHistory?userId=1


