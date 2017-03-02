# myapp

Basic web application written in Clojure. This is a news portal with user-created content which is suplied to end sers. For this app there are two types of users. First type of users are a content creation users. Second type of users are a read-only users. Ther are several user case in this app:
-Showing info about page (both creation and read-only users)
-Showing list of news (both creation and read-only users)
-Showing each of news (both creation and read-only users)
-Creating news (creation user)
-Editing news (creation user)
-Deleting news (creation user)

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2017 FIXME
