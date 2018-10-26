# betting-app

1. Architecture
   This application is mainly built around three main classes based on the key functional areas of the application.

   1. RequestHandler
      This will act as the client request controller and it redirects the request to the relevant services.
      It is implemented the Runnable interface, so that every request will be handled as a separate thread of execution.
      Based on the action it will access the corresponding service class.
      It refer to an interface 'Service' which make sure to implement it's 'serveRequest' method.

   2. SessionService
      This class implements the so called 'Service' interface and take care session management.
      It would maintain a map between customer and the session and also session to customer.
      A new session will be generated for a customer who does not have a session in the map.
      Session will be removed from the system if it is expired on the next time customer access this session.
      If the session is expired, it will be removed from the map. Then user would have to send a brand new request to get a new session.

   3. BettingService
      This class also implements the 'Service' interface and handles the betting related services.
      It also maintains a map to keep track of bet offers (betOfferId -> BetOffer)
      Within the betOffer object, it maintains another map to keep track of different customer records.
      Within a customer record, it maintains all the stakes submit for that offer by customer and also updates the highest stake, every time customer submit a stake.
      So, when the highest stake list is requested, it consider these information and returns the top 20 items.


2. General Concerns

    1. A fairly and simple design is used here to model this application, and object oriented concepts and principle were used as necessary.
    2. Tried to separate out the responsibilities as much as I could and assigned them to different classes.
    3. Used a thread pool to handle user request as it might hit multiple requests concurrently.
       The 'ThreadpoolExecutor' is a smart way to achieve this since it manages the pool related functions and also maintains a queue for incoming requests.
    4. Used data structures which are thread safe and good in performance. HashMaps been used to model the data store as they acts as mini databases.
    5. Used Java 8 features then and there to make the code much simpler(Stream API, Time API)
    6. Unit tests have been written for most of the critical functions as it cover the most scenarios.
       Junit has been used for this purpose and it is the only third party library being used.
