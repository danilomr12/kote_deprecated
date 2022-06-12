# Kote
WARNING: Project outdated with old Grails, Adobe Flex and spring versions - restored from Mercurial-hg source control from bitbucket.
It is almost imposible to run with the gradle/maven dependencies.
Kote is focused on groceries stores, supermarkets and similar business that have the need of frequently buy big ammount of itens from 
wholesale provides, from hudreds to thousands items.
Kote is a Reverse procurement/biding system with focus in finding and buying the lowest prices for each product in a list.
Each seller ends selling only the products they "win".


## Architeture
The aplication consists in a Adobe Flex 3 deployed as a SWF binary, which needs a browser with Flash player to run.
The front end comunicates with backend "Servidor" using an RPC protocol, BlazeDS. This involques the Services Facades from
the backend application written with Grails 1.3.
The Grails app backend is a common grails application that uses only for admin interface the GSP views. For the clients, we put the swf binary inside the webapp.
The application is secured by spring security.
The "middleware" part of the reposistory is an application that works as a microservice comunicating with the backend server only using java RMI. 
It is a pure java with Spring and mongoDB database used for processing the bigest workload of the procurenment system. It stores as a document all the products requested by the buyer with all sellers prices
and process it sorting each item by prices.


## Frontend 
The frontend aplication is a Adobe Flex 3 RIA. 
It is located at Servidor/grails-app/views/flex/
The front end is divided into 3 types of users:
- The Seller
- The Buyer administrator - Creates or start the procurement process, sending to sellers.
- The one in charge for the stock of the store - with no permission to start the procurement process, just creates and needed products to a list.

## Backend
Main Grails application that comunicates with front end and handles users/roles, products catalog, catalog search, comunicates with middleware.
Located at /Servidor

## Middleware
Is the Java application responsible for storing and processing the final steps of the reverse procurement.
Located at /middleware

