(ns myapp.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [myapp.views :as views]
            [ring.util.response :as resp]
            [myapp.posts :as posts]))

(defroutes public-routes
           (GET "/" [] (views/main-page))
           (route/resources "/"))

(defroutes protected-routes
           (GET "/admin" [] (views/admin-blog-page))
           (GET "/admin/add" [] (views/add-post))
           (POST "/admin/create" [& params]
                 (do (posts/create params)
                     (resp/redirect "/admin")))
           (GET "/admin/:id/edit" [id] (views/edit-post id))
           (POST "/admin/:id/save" [& params]
                 (do (posts/save (:id params) params)
                     (resp/redirect "/admin")))
           (GET "/admin/:id/delete" [id]
                (do (posts/delete id)
                    (resp/redirect "/admin"))))

(defroutes app-routes
           public-routes
           protected-routes
           (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
