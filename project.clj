(defproject myapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [hiccup "1.0.4"]
                 [ring-basic-authentication "1.0.2"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler myapp.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
