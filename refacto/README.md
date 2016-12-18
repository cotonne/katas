This kata is "Refactor my controller"

# Steps
## Tests
There are some basic tests with mocks. Something quite common.

However, do some method extraction and rename them to have more relevant
and maintenable code.

## Controller

You have to think of responsibility and single responsibility principle (from SOLID).
What is the aim of a controller?

Maybe you should just retrieve the request and send back the response?

Also, have you thought of CQS (GET and PUT)?

## Business 
The business steps are:
 - Merging the existing customer with its previous record
 - Send the updated customer to the legacy
 - Persist the updated customer in the system
 - Persist the state of the updated customer in the system
 - Send the customer to a partner when it is a man

You should not change the order. For example, if the updated customer is not sent to 
the legacy, everything should be stopped.

